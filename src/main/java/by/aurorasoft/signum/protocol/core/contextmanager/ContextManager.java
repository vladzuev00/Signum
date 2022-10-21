package by.aurorasoft.signum.protocol.core.contextmanager;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.service.CommandService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.*;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Type.COMMAND;
import static io.netty.util.AttributeKey.valueOf;
import static java.lang.String.format;

@Component
public final class ContextManager {
    private static final AttributeKey<Unit> CHANNEL_ATTRIBUTE_KEY_UNIT
            = valueOf("unit");

//    private static final AttributeKey<BlockingQueue<CommandWaitingResponse>>
//            CHANNEL_ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE = valueOf("command_waiting_response");
//    private static final Supplier<BlockingQueue<CommandWaitingResponse>> COMMAND_WAITING_RESPONSE_QUEUE_SUPPLIER
//            = () -> new ArrayBlockingQueue<>(1);

    private static final AttributeKey<Queue<Command>> ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT
            = valueOf("commands_to_be_sent");
    private static final AttributeKey<CommandWaitingResponse> ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE
            = valueOf("command_waiting_response");

    private static final String TEMPLATE_SENT_COMMAND = "#M#%s";

    private final CommandService commandService;
    private final ContextManager contextManager;
    //TODO: seconds
    private final int waitingResponseTimeoutInMinutes;

    public ContextManager(CommandService commandService, ContextManager contextManager,
                          @Value("${netty.contextManager.lifecycleObserver.waitingResponseTimeoutInMinutes}")
                          int waitingResponseTimeoutInMinutes) {
        this.commandService = commandService;
        this.contextManager = contextManager;
        this.waitingResponseTimeoutInMinutes = waitingResponseTimeoutInMinutes;
    }

    public Unit findUnit(ChannelHandlerContext context) {
        return findAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_UNIT);
    }

    public void putUnit(ChannelHandlerContext context, Unit unit) {
        putAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_UNIT, unit);
    }

    @SuppressWarnings("all")
    public void addCommandToBeSent(Command command, ChannelHandlerContext context) {
        initializeCommandsToBeSentAttribute(context);
        synchronized (context) {
            final CommandWaitingResponse commandWaitingResponse
                    = findAttributeValue(context, ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE);
            if (commandWaitingResponse == null) {
                this.sendCommand(context, command);
            } else {
                final Queue<Command> commandsToBeSent
                        = findAttributeValue(context, ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT);
                commandsToBeSent.add(command);
            }
        }
    }

//    /**
//     * Operation is blocked while command waiting response in context.
//     */
//    public Command findCommandWaitingResponse(ChannelHandlerContext context) {
//        final Queue<CommandWaitingResponse> commandsWaitingResponses
//                = findAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE);
//        final CommandWaitingResponse commandWaitingResponse = commandsWaitingResponses.poll();
//        if (commandWaitingResponse == null) {
//            throw new NoSuchElementException("Command waiting response wasn't found.");
//        }
//        commandWaitingResponse.cancelObserverTask();
//        return commandWaitingResponse.getCommand();
//    }
//
//    /**
//     * Operation is blocked while other command waiting response in context.
//     */
//    public void putCommandWaitingResponse(ChannelHandlerContext context, Command command) {
//        initializeCommandWaitingResponseQueue(context);
//        try {
//            final ScheduledFuture<?> observerLifecycleTask = context
//                    .channel()
//                    .eventLoop()
//                    .schedule(this.createObserverLifecycleCommandTask(context, command),
//                            this.waitingResponseTimeoutInMinutes, MINUTES);
//            final CommandWaitingResponse commandWaitingResponse
//                    = new CommandWaitingResponse(command, observerLifecycleTask);
//            final BlockingQueue<CommandWaitingResponse> commandWaitingResponseQueue
//                    = findAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE);
//            commandWaitingResponseQueue.put(commandWaitingResponse);
//        } catch (final InterruptedException cause) {
//            throw new ContextManagingException(cause);
//        }
//    }

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

    @SuppressWarnings("all")
    private static void initializeCommandsToBeSentAttribute(ChannelHandlerContext context) {
        synchronized (context) {
            final Queue<Command> commandsToBeSent = findAttributeValue(context, ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT);
            if (commandsToBeSent == null) {
                putAttributeValue(context, ATTRIBUTE_KEY_COMMANDS_TO_BE_SENT, new LinkedList<>());
            }
        }
    }

    private void sendCommand(ChannelHandlerContext context, Command command) {
        final Command savedCommand = this.commandService.save(command, NEW, COMMAND);
        final String sentCommand = format(TEMPLATE_SENT_COMMAND, savedCommand.getText());

        context.writeAndFlush(sentCommand)
                .addListener(
                        (ChannelFutureListener)
                                future -> this.commandService.updateByGivenStatus(savedCommand, SENT));
    }

//    @SuppressWarnings("all")
//    private static void initializeCommandWaitingResponseQueue(ChannelHandlerContext context) {
//        synchronized (context) {
//            final BlockingQueue<CommandWaitingResponse> commandWaitingResponseQueue
//                    = findAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE);
//            if (commandWaitingResponseQueue == null) {
//                putAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE,
//                        COMMAND_WAITING_RESPONSE_QUEUE_SUPPLIER.get());
//            }
//        }
//    }
//
//    private Runnable createObserverLifecycleCommandTask(ChannelHandlerContext context, Command command) {
//        return () -> {
//            try {
//                if (!interrupted()) {
//                    final BlockingQueue<CommandWaitingResponse> commandsQueue
//                            = findAttributeValue(context, CHANNEL_ATTRIBUTE_KEY_COMMAND_WAITING_RESPONSE);
//                    commandsQueue.take();
//                    this.commandService.save(command, TIMEOUT, COMMAND);
//                }
//            } catch (final InterruptedException cause) {
//                throw new ContextManagingException(cause);
//            }
//        };
//    }

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
