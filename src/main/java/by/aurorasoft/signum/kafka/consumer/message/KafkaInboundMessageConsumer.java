package by.aurorasoft.signum.kafka.consumer.message;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.service.MessageService;
import by.aurorasoft.signum.kafka.producer.KafkaSavedMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public final class KafkaInboundMessageConsumer extends AbstractKafkaMessageConsumer {
    private static final String LOG_TEMPLATE_RECEIVE_INBOUND_MESSAGES_FROM_KAFKA
            = "Inbound messages were received from kafka. Messages: %s.";
    private static final String LOG_TEMPLATE_SAVE_INBOUND_MESSAGES = "Inbound messages were saved. Messages: %s";

    private final MessageService messageService;
    private final KafkaSavedMessageProducer savedMessageProducer;

    @Override
    @KafkaListener(
            topics = "${kafka.topic.inbound-messages.name}",
            groupId = "${kafka.topic.inbound-messages.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactoryInboundMessages")
    public void listen(List<ConsumerRecord<Long, GenericRecord>> records) {
        final List<Message> messages = super.map(records);
        log.info(format(LOG_TEMPLATE_RECEIVE_INBOUND_MESSAGES_FROM_KAFKA, messages));
        final List<Message> savedMessages = this.messageService.saveAll(messages);
        log.info(format(LOG_TEMPLATE_SAVE_INBOUND_MESSAGES, messages));
        this.savedMessageProducer.send(savedMessages);
    }
}
