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

    @Bean
    @Autowired
    public KafkaTemplate<Long, GenericRecord> kafkaTemplateInboundMessage(
            @Value("${kafka.topic.inbound-messages.producer.batch-size}") int batchSize,
            @Value("${kafka.topic.inbound-messages.producer.linger-ms}") int lingerMs,
            @Value("${kafka.topic.inbound-messages.producer.delivery-timeout-ms}") int deliveryTimeoutMs,
            @Qualifier("transportableMessageSchema") Schema schema) {
        return new KafkaTemplate<>(
                this.createInboundMessageProducerFactory(batchSize, lingerMs, deliveryTimeoutMs, schema));
    }

    private ProducerFactory<Long, GenericRecord> createInboundMessageProducerFactory(int batchSize, int lingerMs,
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
