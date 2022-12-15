package by.aurorasoft.signum.kafka.consumer.message;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public final class KafkaSavedMessageConsumer extends AbstractKafkaMessageConsumer {

    @Override
    @KafkaListener(
            topics = "${kafka.topic.saved-messages.name}",
            groupId = "${kafka.topic.saved-messages.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactorySavedMessages")
    public void listen(List<ConsumerRecord<Long, GenericRecord>> records) {

    }
}
