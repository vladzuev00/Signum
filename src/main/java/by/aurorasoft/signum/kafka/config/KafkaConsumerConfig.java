package by.aurorasoft.signum.kafka.config;

import by.aurorasoft.kafka.serialize.AvroGenericRecordDeserializer;
import by.aurorasoft.signum.kafka.producer.transportable.TransportableMessage;
import org.apache.avro.Schema;
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
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;

import static by.aurorasoft.kafka.variables.KafkaVars.SCHEMA_PROP_NAME;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    @Autowired
    public ConsumerFactory<Long, TransportableMessage> consumerFactoryInboundMessage(
            @Value("${kafka.topic.inbound-messages.consumer.group-id}") String groupId,
            @Qualifier("transportableMessageSchema") Schema schema) {
        final Map<String, Object> configurationProperties = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, this.bootstrapAddress,
                GROUP_ID_CONFIG, groupId,
                KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, AvroGenericRecordDeserializer.class,
                SCHEMA_PROP_NAME, schema,
                "value.deserializer.specific.avro.reader", true);
        return new DefaultKafkaConsumerFactory<>(configurationProperties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, TransportableMessage> kafkaListenerContainerFactoryInboundMessage(
            @Value("${kafka.topic.inbound-messages.consumer.group-id}") String groupId,
            @Qualifier("transportableMessageSchema") Schema schema) {
        final ConcurrentKafkaListenerContainerFactory<Long, TransportableMessage> listenerContainerFactory
                = new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setBatchListener(true);
        listenerContainerFactory.setConsumerFactory(this.consumerFactoryInboundMessage(groupId, schema));
        listenerContainerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return listenerContainerFactory;
    }
}
