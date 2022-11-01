package by.aurorasoft.signum.protocol.core.contextmanager;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.wialon.service.sendcommand.CommandSenderService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.*;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.*;
import static io.netty.util.AttributeKey.valueOf;
import static java.lang.Thread.interrupted;
import static java.util.Optional.empty;
import static java.util.concurrent.TimeUnit.SECONDS;

//TODO: correct dependencies
@Component
public final class ContextManager {
    private static final AttributeKey<String> ATTRIBUTE_KEY_DEVICE_IMEI = valueOf("device_imei");
    private static final AttributeKey<Device> ATTRIBUTE_KEY_DEVICE = valueOf("device");
    private static final AttributeKey<CommandWaitingResponse> ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE
            = valueOf("command_waiting_response");
    private static final AttributeKey<Queue<Command>> ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT
            = valueOf("commands_to_be_sent");

    private final CommandService commandService;
    private final CommandSenderService commandSenderService;
    private final int waitingResponseTimeoutInSeconds;

    public ContextManager(CommandService commandService,
                          @Lazy CommandSenderService commandSenderService,
                          @Value("${netty.contextManager.lifecycleObserver.waitingResponseTimeoutInSeconds}")
                          int waitingResponseTimeoutInSeconds) {
        this.commandService = commandService;
        this.commandSenderService = commandSenderService;
        this.waitingResponseTimeoutInSeconds = waitingResponseTimeoutInSeconds;
    }

    public void putDeviceImei(ChannelHandlerContext context, String imei) {
        putAttributeValue(context, ATTRIBUTE_KEY_DEVICE_IMEI, imei);
    }

    public String findDeviceImei(ChannelHandlerContext context) {
        return findAttributeValue(context, ATTRIBUTE_KEY_DEVICE_IMEI);
    }

    public Device findDevice(ChannelHandlerContext context) {
        return findAttributeValue(context, ATTRIBUTE_KEY_DEVICE);
    }

    public void putDevice(ChannelHandlerContext context, Device device) {
        putAttributeValue(context, ATTRIBUTE_KEY_DEVICE, device);
    }

    public boolean isExistCommandWaitingResponse(ChannelHandlerContext context) {
        return findAttributeValue(context, ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE) != null;
    }

    public void putCommandWaitingResponse(ChannelHandlerContext context, Command command) {
        final ScheduledFuture<?> observerLifecycleTask = this.runObserverLifecycleCommandTask(context, command);
        final CommandWaitingResponse commandWaitingResponse
                = new CommandWaitingResponse(command, observerLifecycleTask);
        putAttributeValue(context, ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE, commandWaitingResponse);
    }

    public Optional<Command> findCommandWaitingResponse(ChannelHandlerContext context) {
        final CommandWaitingResponse commandWaitingResponse
                = findAttributeValue(context, ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE);
        return commandWaitingResponse != null
                ? Optional.of(commandWaitingResponse.getCommand())
                : empty();
    }

    public void onGetCommandResponse(ChannelHandlerContext context) {
        final CommandWaitingResponse commandWaitingResponse
                = findAttributeValue(context, ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE);
        removeCommandWaitingResponse(context);
        commandWaitingResponse.cancelObserverTask();
        this.commandSenderService.onSentCommandWasHandled(context);
    }

    public void putCommandToBeSent(ChannelHandlerContext context, Command command) {
        initializeCommandsToBeSentAttributeIfNecessary(context);
        final Queue<Command> commandsToBeSent = findAttributeValue(context, ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT);
        commandsToBeSent.add(command);
    }

    public Command findCommandToBeSent(ChannelHandlerContext context) {
        final Queue<Command> commandsToBeSent = findAttributeValue(context, ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT);
        return commandsToBeSent.poll();
    }

    public boolean isExistCommandToBeSent(ChannelHandlerContext context) {
        final Queue<Command> commandsToBeSent = findAttributeValue(context, ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT);
        return commandsToBeSent != null && !commandsToBeSent.isEmpty();
    }

    private static <ValueType> ValueType findAttributeValue(ChannelHandlerContext context,
                                                            AttributeKey<ValueType> attributeKey) {
        final Channel channel = context.channel();
        final Attribute<ValueType> attribute = channel.attr(attributeKey);
        return attribute.get();
    }

    private static <ValueType> void putAttributeValue(ChannelHandlerContext context,
                                                      AttributeKey<ValueType> attributeKey,
                                                      ValueType value) {
        final Channel channel = context.channel();
        final Attribute<ValueType> attribute = channel.attr(attributeKey);
        attribute.set(value);
    }

    private ScheduledFuture<?> runObserverLifecycleCommandTask(ChannelHandlerContext context, Command command) {
        return context
                .channel()
                .eventLoop()
                .schedule(this.createObserverLifecycleCommandTask(context, command),
                        this.waitingResponseTimeoutInSeconds, SECONDS);
    }

    private Runnable createObserverLifecycleCommandTask(ChannelHandlerContext context, Command command) {
        return () -> {
            if (!interrupted()) {
                removeCommandWaitingResponse(context);
                this.commandService.updateByStatus(command, TIMEOUT);
                this.commandSenderService.onSentCommandWasHandled(context);
            }
        };
    }

    private static void removeCommandWaitingResponse(ChannelHandlerContext context) {
        putAttributeValue(context, ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE, null);
    }

    @SuppressWarnings("all")
    private static void initializeCommandsToBeSentAttributeIfNecessary(ChannelHandlerContext context) {
        if (findAttributeValue(context, ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT) == null) {
            synchronized (context) {
                if (findAttributeValue(context, ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT) == null) {
                    putAttributeValue(context, ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT, new ConcurrentLinkedQueue<>());
                }
            }
        }
    }

    @lombok.Value
    private static class CommandWaitingResponse {
        private static final boolean MAY_INTERRUPT_IF_RUNNING_FOR_CANCELING_TASK = true;

        Command command;

        ScheduledFuture<?> observerLifecycleTask;

        public void cancelObserverTask() {
            this.observerLifecycleTask.cancel(MAY_INTERRUPT_IF_RUNNING_FOR_CANCELING_TASK);
        }
    }
}
