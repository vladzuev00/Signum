package by.aurorasoft.signum.kafka.producer;

import by.aurorasoft.kafka.producer.KafkaProducerGenericRecordIntermediaryHooks;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.kafka.producer.converter.MessageToTransportableConverter;
import by.aurorasoft.signum.kafka.producer.transportable.TransportableMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static java.lang.String.format;

@Slf4j
public abstract class AbstractKafkaMessageProducer
        extends KafkaProducerGenericRecordIntermediaryHooks<Long, TransportableMessage, Message> {
    private static final String LOG_START_SENDING_MESSAGES
            = "Sending messages to kafka is started. Amount messages: %d. Topic: %s";
    private static final String LOG_END_SENDING_MESSAGES
            = "Messages were sent to kafka. Amount messages: %d. Topic: %s";
    private static final String LOG_MESSAGE_WAS_SENT_FAILURE
            = "Sending message to kafka was failed. Message: %s. Topic: %s";

    private final MessageToTransportableConverter converter;

    public AbstractKafkaMessageProducer(String topicName, KafkaTemplate<Long, GenericRecord> kafkaTemplate,
                                        Schema schema, MessageToTransportableConverter converter) {
        super(topicName, kafkaTemplate, schema);
        this.converter = converter;
    }

    public final void send(List<Message> messages) {
        log.info(format(LOG_START_SENDING_MESSAGES, messages.size(), super.topicName));
        super.send(messages);
        log.info(format(LOG_END_SENDING_MESSAGES, messages.size(), super.topicName));
    }

    @Override
    protected final TransportableMessage convertModelToTransportable(Message message) {
        return this.converter.convert(message);
    }

    @Override
    protected final Long getTopicKey(Message message) {
        return message.getDeviceId();
    }

    @Override
    protected final void onSendFailure(Message message, Throwable throwable) {
        log.error(format(LOG_MESSAGE_WAS_SENT_FAILURE, message, super.topicName));
        throwable.printStackTrace();
    }
}
