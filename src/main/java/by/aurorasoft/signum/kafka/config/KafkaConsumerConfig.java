package by.aurorasoft.signum.kafka.config;

import by.aurorasoft.kafka.serialize.AvroGenericRecordDeserializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

import static by.aurorasoft.kafka.variables.KafkaVars.SCHEMA_PROP_NAME;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    @Autowired
    public ConsumerFactory<Long, GenericRecord> consumerFactoryInboundMessages(
            @Value("${kafka.topic.inbound-messages.consumer.group-id}") String groupId,
            @Value("${kafka.topic.inbound-messages.consumer.max-poll-records}") int maxPollRecords,
            @Value("${kafka.topic.inbound-messages.consumer.fetch-max-wait-ms}") int fetchMaxWaitMs,
            @Value("${kafka.topic.inbound-messages.consumer.fetch-min-bytes}") int fetchMinBytes,
            @Qualifier("transportableMessageSchema") Schema schema) {
        return this.createConsumerFactory(groupId, maxPollRecords, fetchMaxWaitMs, fetchMinBytes, schema);
    }

    @Bean
    @Autowired
    public ConcurrentKafkaListenerContainerFactory<Long, GenericRecord> kafkaListenerContainerFactoryInboundMessages(
            @Qualifier("consumerFactoryInboundMessages") ConsumerFactory<Long, GenericRecord> consumerFactoryInboundMessages) {
        return createKafkaListenerContainerFactory(consumerFactoryInboundMessages);
    }

    @Bean
    @Autowired
    public ConsumerFactory<Long, GenericRecord> consumerFactorySavedMessages(
            @Value("${kafka.topic.saved-messages.consumer.group-id}") String groupId,
            @Value("${kafka.topic.saved-messages.consumer.max-poll-records}") int maxPollRecords,
            @Value("${kafka.topic.saved-messages.consumer.fetch-max-wait-ms}") int fetchMaxWaitMs,
            @Value("${kafka.topic.saved-messages.consumer.fetch-min-bytes}") int fetchMinBytes,
            @Qualifier("transportableMessageSchema") Schema schema) {
        return this.createConsumerFactory(groupId, maxPollRecords, fetchMaxWaitMs, fetchMinBytes, schema);
    }

    @Bean
    @Autowired
    public ConcurrentKafkaListenerContainerFactory<Long, GenericRecord> kafkaListenerContainerFactorySavedMessages(
            @Qualifier("consumerFactorySavedMessages") ConsumerFactory<Long, GenericRecord> consumerFactorySavedMessages) {
        return createKafkaListenerContainerFactory(consumerFactorySavedMessages);
    }

    private <KeyType, ValueType> ConsumerFactory<KeyType, ValueType> createConsumerFactory(String groupId,
                                                                                           int maxPollRecords,
                                                                                           int fetchMaxWaitMs,
                                                                                           int fetchMinBytes,
                                                                                           Schema schema) {
        final Map<String, Object> configurationProperties = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, this.bootstrapAddress,
                GROUP_ID_CONFIG, groupId,
                KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, AvroGenericRecordDeserializer.class,
                MAX_POLL_RECORDS_CONFIG, maxPollRecords,
                FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs,
                FETCH_MIN_BYTES_CONFIG, fetchMinBytes,
                ENABLE_AUTO_COMMIT_CONFIG, false,
                SCHEMA_PROP_NAME, schema);
        return new DefaultKafkaConsumerFactory<>(configurationProperties);
    }

    private static <KeyType, ValueType> ConcurrentKafkaListenerContainerFactory<KeyType, ValueType> createKafkaListenerContainerFactory(
            ConsumerFactory<KeyType, ValueType> consumerFactory) {
        final ConcurrentKafkaListenerContainerFactory<KeyType, ValueType> listenerContainerFactory
                = new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setBatchListener(true);
        listenerContainerFactory.setConsumerFactory(consumerFactory);
        listenerContainerFactory.getContainerProperties().setAckMode(MANUAL_IMMEDIATE);
        return listenerContainerFactory;
    }
}
