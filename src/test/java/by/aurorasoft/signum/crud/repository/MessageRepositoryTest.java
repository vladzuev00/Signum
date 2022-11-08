package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;
import static java.time.Instant.now;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class MessageRepositoryTest extends AbstractContextTest {
    @Autowired
    private MessageRepository repository;

    @Test
    public void messageShouldBeInserted() {
        final MessageEntity givenMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .dateTime(now())
                .latitude(5.5F)
                .longitude(6.6F)
                .speed(10)
                .course(11)
                .altitude(12)
                .amountSatellite(13)
                .gsmLevel(54)
                .onboardVoltage(7.7F)
                .ecoCornering(8.8F)
                .ecoAcceleration(9.9F)
                .ecoBraking(10.1F)
                .type(VALID)
                .build();
        super.startQueryCount();
        this.repository.save(givenMessage);
        super.checkQueryCount(1);
    }

    @Test
    @Sql("classpath:sql/insert-message.sql")
    public void messageShouldBeFoundById() {
        super.startQueryCount();
        final MessageEntity foundMessage = this.repository.findById(25551L).orElseThrow();
        super.checkQueryCount(1);

        assertEquals(25551, foundMessage.getId().longValue());
        assertEquals(parse("2000-02-18T04:05:06Z"), foundMessage.getDatetime());
        assertEquals(4.4, foundMessage.getLatitude(), 0.001);
        assertEquals(5.5, foundMessage.getLongitude(), 0.001);
        assertEquals(10, foundMessage.getSpeed());
        assertEquals(11, foundMessage.getCourse());
        assertEquals(12, foundMessage.getAltitude());
        assertEquals(13, foundMessage.getAmountSatellite());
        assertEquals(25551, foundMessage.getDevice().getId().longValue());
        assertEquals(44, foundMessage.getGsmLevel());
        assertEquals(1.5, foundMessage.getOnboardVoltage(), 0.001);
        assertEquals(1.6, foundMessage.getEcoCornering(), 0.001);
        assertEquals(1.7, foundMessage.getEcoAcceleration(), 0.001);
        assertEquals(1.8, foundMessage.getEcoBraking(), 0.001);
        assertSame(VALID, foundMessage.getType());
    }
}
