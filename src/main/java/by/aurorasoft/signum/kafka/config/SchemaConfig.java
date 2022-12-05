package by.aurorasoft.signum.kafka.config;

import by.aurorasoft.signum.kafka.producer.transportable.TransportableMessage;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaConfig {
    private final ReflectData reflectData;

    public SchemaConfig() {
        this.reflectData = ReflectData.get();
    }

    @Bean
    public Schema transportableMessageSchema() {
        return this.reflectData.getSchema(TransportableMessage.class);
    }
}
