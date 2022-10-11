package by.aurorasoft.signum.base;

import by.aurorasoft.signum.crud.model.entity.BaseEntity;
import com.yannbriancon.interceptor.HibernateQueryInterceptor;
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
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.lang.reflect.Constructor;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {AbstractContextTest.DBContainerInitializer.class})
public abstract class AbstractContextTest {

    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

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

    protected static <EntityType extends BaseEntity> EntityType createEntity(Long id,
                                                                             Class<? extends EntityType> entityType) {
        try {
            final Constructor<? extends EntityType> constructor = entityType.getConstructor();
            final EntityType createEntity = constructor.newInstance();
            createEntity.setId(id);
            return createEntity;
        } catch (final Exception cause) {
            throw new RuntimeException(cause);
        }
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
}
