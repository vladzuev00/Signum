package by.aurorasoft.signum.protocol.core.contextmanager;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.wialon.service.sendcommand.CommandSenderService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.*;
import static io.netty.util.AttributeKey.valueOf;
import static java.lang.Thread.interrupted;
import static java.util.concurrent.TimeUnit.SECONDS;

//TODO: correct dependencies
@Component
public final class ContextManager {
    private static final AttributeKey<Unit> CHANNEL_ATTRIBUTE_KEY_UNIT
            = valueOf("unit");
    private static final AttributeKey<CommandWaitingResponse> ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE
            = valueOf("command_waiting_response");

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

    public Unit findUnit(ChannelHandlerContext context) {
        return findAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_UNIT);
    }

    public void putUnit(ChannelHandlerContext context, Unit unit) {
        putAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_UNIT, unit);
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

    public Command findCommandWaitingResponse(ChannelHandlerContext context) {
        final CommandWaitingResponse commandWaitingResponse
                = findAttributeValue(context, ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE);
        return commandWaitingResponse.getCommand();
    }

    public void onGetCommandResponse(ChannelHandlerContext context) {
        final CommandWaitingResponse commandWaitingResponse
                = findAttributeValue(context, ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE);
        removeCommandWaitingResponse(context);
        commandWaitingResponse.cancelObserverTask();
        final Command command = commandWaitingResponse.getCommand();
        this.commandSenderService.onSentCommandWasHandled(command.getDevice());
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
                this.commandService.updateByGivenStatus(command, TIMEOUT);
                this.commandSenderService.onSentCommandWasHandled(command.getDevice());
            }
        };
    }

    private static void removeCommandWaitingResponse(ChannelHandlerContext context) {
        putAttributeValue(context, ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE, null);
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
