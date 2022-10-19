package by.aurorasoft.signum.protocol.wialon.contextmanager;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.service.CommandService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.*;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.TIMEOUT_NOT_ANSWERED;
import static io.netty.util.AttributeKey.valueOf;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.MINUTES;

@Component
public final class ContextManager implements AutoCloseable {
    private static final String NAME_CHANNEL_ATTRIBUTE_KEY_UNIT = "unit";
    private static final AttributeKey<Unit> CHANNEL_ATTRIBUTE_KEY_UNIT
            = valueOf(NAME_CHANNEL_ATTRIBUTE_KEY_UNIT);

    private static final String NAME_CHANNEL_ATTRIBUTE_KEY_COMMANDS_WAITING_RESPONSES
            = "commands_waiting_responses";
    private static final AttributeKey<Queue<CommandWaitingResponse>> CHANNEL_ATTRIBUTE_KEY_COMMANDS_WAITING_RESPONSES
            = valueOf(NAME_CHANNEL_ATTRIBUTE_KEY_COMMANDS_WAITING_RESPONSES);

    private final ScheduledExecutorService executorService;
    private final CommandService commandService;

    public ContextManager(CommandService commandService) {
        this.executorService = newSingleThreadScheduledExecutor();
        this.commandService = commandService;
    }

    public Unit findUnit(ChannelHandlerContext context) {
        return findAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_UNIT);
    }

    public void putUnit(ChannelHandlerContext context, Unit unit) {
        putAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_UNIT, unit);
    }

    public Command findCommandWaitingResponse(ChannelHandlerContext context) {
        final Queue<CommandWaitingResponse> commandsWaitingResponses
                = findAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_COMMANDS_WAITING_RESPONSES);
        final CommandWaitingResponse commandWaitingResponse = commandsWaitingResponses.poll();
        if (commandWaitingResponse == null) {
            throw new NoSuchElementException("Command waiting response wasn't found.");
        }
        commandWaitingResponse.cancelObserverTask();
        return commandWaitingResponse.getCommand();
    }

    public void putCommandWaitingResponse(ChannelHandlerContext context, Command command) {
        Queue<CommandWaitingResponse> commandsWaitingResponses = findAttributeValue(context,
                CHANNEL_ATTRIBUTE_KEY_COMMANDS_WAITING_RESPONSES);
        if (commandsWaitingResponses == null) {
            commandsWaitingResponses = new LinkedList<>();
            putAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_COMMANDS_WAITING_RESPONSES,
                    commandsWaitingResponses);
        }
        final Future<?> observerLifecycleTask = this.executorService.schedule(() -> {
            if (!currentThread().isInterrupted()) {
                final Queue<CommandWaitingResponse> commandsQueue = findAttributeValue(context,
                        CHANNEL_ATTRIBUTE_KEY_COMMANDS_WAITING_RESPONSES);
                commandsQueue.poll();
                this.commandService.saveWithGivenStatus(command, TIMEOUT_NOT_ANSWERED);
            }
        }, 5, MINUTES);      //TODO: брать из проперти файла
        final CommandWaitingResponse commandWaitingResponse
                = new CommandWaitingResponse(command, observerLifecycleTask);
        commandsWaitingResponses.add(commandWaitingResponse);
    }

    @PreDestroy
    @Override
    public void close() {
        this.executorService.shutdownNow();
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

    @Value
    private static class CommandWaitingResponse {
        private static final boolean MAY_INTERRUPT_IF_RUNNING_FOR_CANCELING_TASK = true;

        Command command;

        Future<?> observerLifecycleTask;

        public void cancelObserverTask() {
            this.observerLifecycleTask.cancel(MAY_INTERRUPT_IF_RUNNING_FOR_CANCELING_TASK);
        }
    }
}
