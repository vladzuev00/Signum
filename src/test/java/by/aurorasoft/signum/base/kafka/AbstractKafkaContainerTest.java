package by.aurorasoft.signum.base.kafka;

import by.aurorasoft.signum.base.AbstractContextTest;
import org.junit.ClassRule;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@ContextConfiguration(initializers = { AbstractKafkaContainerTest.KafkaBootstrapAddressOverrider.class })
public abstract class AbstractKafkaContainerTest extends AbstractContextTest {

    @ClassRule
    public static KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    public static class KafkaBootstrapAddressOverrider
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @SuppressWarnings("NullableProblems")
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers());
        }
    }
}
