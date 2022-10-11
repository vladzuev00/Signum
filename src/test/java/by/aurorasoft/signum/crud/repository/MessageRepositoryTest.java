package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static java.time.Instant.now;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class MessageRepositoryTest extends AbstractContextTest {

    @Autowired
    private MessageRepository repository;

    @Sql("classpath:sql/insert-unit.sql")
    @Test
    public void messageShouldBeInserted() {
        final MessageEntity givenMessage = new MessageEntity(createEntity(1L, UnitEntity.class), now(), 5.5f,
                6.6f, 10, 11, 12, 13, 7.7f, "name:value");
        super.startQueryCount();
        this.repository.save(givenMessage);
        super.checkQueryCount(1);
    }

    @Sql("classpath:sql/insert-message.sql")
    @Test
    public void messageShouldBeFoundById() {
        super.startQueryCount();
        final Optional<MessageEntity> optionalMessage = this.repository.findById(1L);
        super.checkQueryCount(1);

        assertTrue(optionalMessage.isPresent());
        final MessageEntity message = optionalMessage.get();
        assertEquals(1, message.getId().longValue());
        assertEquals(1, message.getUnit().getId().longValue());
        assertEquals(parse("2000-02-18T04:05:06Z"), message.getDateTime());
        assertEquals(4.4, message.getLatitude(), 0.001);
        assertEquals(5.5, message.getLongitude(), 0.001);
        assertEquals(10, message.getSpeed());
        assertEquals(11, message.getCourse());
        assertEquals(12, message.getAltitude());
        assertEquals(13, message.getAmountSatellite());
        assertEquals(6.6, message.getHdop(), 0.001);
        assertEquals("param1:value1,param2:value2", message.getParameters());
    }
}
