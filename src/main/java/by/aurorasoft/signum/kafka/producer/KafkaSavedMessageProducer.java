package by.aurorasoft.signum.kafka.producer;

import by.aurorasoft.signum.kafka.producer.converter.MessageToTransportableConverter;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public final class KafkaSavedMessageProducer extends AbstractKafkaMessageProducer {

    public KafkaSavedMessageProducer(@Value("${kafka.topic.saved-messages.name}") String topicName,
                                     KafkaTemplate<Long, GenericRecord> kafkaTemplate,
                                     Schema schema, MessageToTransportableConverter converter) {
        super(topicName, kafkaTemplate, schema, converter);
    }
}
