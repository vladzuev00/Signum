package by.aurorasoft.signum.base;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.yannbriancon.interceptor.HibernateQueryInterceptor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.TimeZone;

import static java.lang.Math.abs;
import static org.junit.Assert.assertEquals;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {AbstractContextTest.DBContainerInitializer.class, AbstractContextTest.KafkaBootstrapAddressOverrider.class})
public abstract class AbstractContextTest {
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @ClassRule
    public static KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    private static final double ALLOWABLE_INACCURACY = 0.001;

    static {
        postgreSQLContainer.start();
    }

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected HibernateQueryInterceptor queryInterceptor;

    @BeforeClass
    public static void setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    protected final void startQueryCount() {
        System.out.println("======================= START QUERY COUNTER ====================================");
        this.queryInterceptor.startQueryCount();
    }

    protected final Long getQueryCount() {
        return this.queryInterceptor.getQueryCount();
    }

    protected final void checkQueryCount(int expected) {
        this.entityManager.flush();
        System.out.println("======================= FINISH QUERY COUNTER ====================================");
        assertEquals("wrong count of queries", Long.valueOf(expected), this.getQueryCount());
    }

    protected <IdType, EntityType extends AbstractEntity<IdType>> EntityType findEntityFromDB(
            Class<? extends EntityType> entityType, IdType id) {
        this.entityManager.flush();
        this.entityManager.clear();
        return this.entityManager.find(entityType, id);
    }

    protected <EntityType extends AbstractEntity<?>> List<EntityType> findEntities(
            Class<EntityType> entityType) {
        return this.entityManager.createQuery("SELECT e FROM " + entityType.getName() + " e", entityType)
                .getResultList();
    }

    protected static boolean areEqualsWithInaccuracy(double expected, double actual) {
        return abs(expected - actual) <= ALLOWABLE_INACCURACY;
    }

    protected static boolean areEqualsWithInaccuracy(float expected, float actual) {
        return abs(expected - actual) <= ALLOWABLE_INACCURACY;
    }

    static class DBContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    public static class KafkaBootstrapAddressOverrider
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers());
        }
    }
}
