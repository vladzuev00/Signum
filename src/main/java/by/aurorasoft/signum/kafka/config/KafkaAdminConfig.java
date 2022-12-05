package by.aurorasoft.signum.kafka.config;

import by.aurorasoft.kafka.topic.KafkaAdminFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaAdminConfig {

    @Bean
    public KafkaAdmin kafkaAdmin(@Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress) {
        return KafkaAdminFactory.create(bootstrapAddress);
    }
}
