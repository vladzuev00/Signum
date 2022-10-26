package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static java.time.Instant.now;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;

public final class MessageRepositoryTest extends AbstractContextTest {

    @Autowired
    private MessageRepository repository;

    @Test
    public void messageShouldBeInserted() {
//        final MessageEntity givenMessage = new MessageEntity(null,
//                super.entityManager.getReference(UnitEntity.class, 25551L),
//                now(), 5.5f, 6.6f, 10, 11, 12, 13,
//                7.7f, "name:value");
//        super.startQueryCount();
//        this.repository.save(givenMessage);
//        super.checkQueryCount(1);
    }

    @Test
    @Sql("classpath:sql/insert-message.sql")
    public void messageShouldBeFoundById() {
//        super.startQueryCount();
//        final MessageEntity message = this.repository.findById(25551L).orElseThrow();
//        super.checkQueryCount(1);
//
//        assertEquals(25551, message.getId().longValue());
//        assertEquals(25551, message.getUnit().getId().longValue());
//        assertEquals(parse("2000-02-18T04:05:06Z"), message.getDateTime());
//        assertEquals(4.4, message.getLatitude(), 0.001);
//        assertEquals(5.5, message.getLongitude(), 0.001);
//        assertEquals(10, message.getSpeed());
//        assertEquals(11, message.getCourse());
//        assertEquals(12, message.getAltitude());
//        assertEquals(13, message.getAmountSatellite());
//        assertEquals(6.6, message.getHdop(), 0.001);
//        assertEquals("param1:value1,param2:value2", message.getParameters());
    }
}
