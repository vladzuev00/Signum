package by.aurorasoft.signum.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic inboundMessagesTopic(
            @Value("${kafka.topic.inbound-messages.name}") String topicName,
            @Value("${kafka.topic.inbound-messages.partitions-amount}") int partitionsAmount,
            @Value("${kafka.topic.inbound-messages.replication-factor}") int replicationFactor) {
        return TopicBuilder
                .name(topicName)
                .partitions(partitionsAmount)
                .replicas(replicationFactor)
                .build();
    }

    @Bean
    public NewTopic savedMessagesTopic(
            @Value("${kafka.topic.saved-messages.name}") String topicName,
            @Value("${kafka.topic.saved-messages.partitions-amount}") int partitionsAmount,
            @Value("${kafka.topic.saved-messages.replication-factor}") int replicationFactor) {
        return TopicBuilder
                .name(topicName)
                .partitions(partitionsAmount)
                .replicas(replicationFactor)
                .build();
    }
}
