package by.aurorasoft.signum.kafka.config;

import by.aurorasoft.kafka.serialize.AvroGenericRecordSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

import static by.aurorasoft.kafka.variables.KafkaVars.SCHEMA_PROP_NAME;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public class KafkaProducerConfig {
    private static final String COMPRESSION_TYPE_CONFIG_SNAPPY = "snappy";

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Kafka template for producer, which sends messages, which haven't been saved in database yet, to kafka.
     */
    @Bean
    @Autowired
    public KafkaTemplate<Long, GenericRecord> kafkaTemplateInboundMessages(
            @Value("${kafka.topic.inbound-messages.producer.batch-size}") int batchSize,
            @Value("${kafka.topic.inbound-messages.producer.linger-ms}") int lingerMs,
            @Value("${kafka.topic.inbound-messages.producer.delivery-timeout-ms}") int deliveryTimeoutMs,
            @Qualifier("transportableMessageSchema") Schema schema) {
        return new KafkaTemplate<>(this.createProducerFactory(batchSize, lingerMs, deliveryTimeoutMs, schema));
    }

    /**
     * Kafka template for producer, which sends messages, which have been saved in database, to kafka.
     */
    @Bean
    @Autowired
    public KafkaTemplate<Long, GenericRecord> kafkaTemplateSavedMessages(
            @Value("${kafka.topic.saved-messages.producer.batch-size}") int batchSize,
            @Value("${kafka.topic.saved-messages.producer.linger-ms}") int lingerMs,
            @Value("${kafka.topic.saved-messages.producer.delivery-timeout-ms}") int deliveryTimeoutMs,
            @Qualifier("transportableMessageSchema") Schema schema) {
        return new KafkaTemplate<>(this.createProducerFactory(batchSize, lingerMs, deliveryTimeoutMs, schema));
    }

    private <KeyType, ValueType> ProducerFactory<KeyType, ValueType> createProducerFactory(int batchSize, int lingerMs,
                                                                                           int deliveryTimeoutMs,
                                                                                           Schema schema) {
        final Map<String, Object> configProps = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, this.bootstrapAddress,
                KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
                VALUE_SERIALIZER_CLASS_CONFIG, AvroGenericRecordSerializer.class,
                COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE_CONFIG_SNAPPY,
                BATCH_SIZE_CONFIG, batchSize,
                LINGER_MS_CONFIG, lingerMs,
                DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs,
                SCHEMA_PROP_NAME, schema);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
}
