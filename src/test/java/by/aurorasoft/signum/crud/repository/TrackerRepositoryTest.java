package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.TrackerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertEquals;

public final class TrackerRepositoryTest extends AbstractContextTest {
    @Autowired
    private TrackerRepository repository;

    @Test
    public void trackerShouldBeInserted() {
        final TrackerEntity tracker = new TrackerEntity("11111222223333344444", "448447045");
        super.startQueryCount();
        this.repository.save(tracker);
        super.checkQueryCount(1);
    }

    @Sql(statements = "INSERT INTO tracker(id, imei, phone_number) VALUES(1, '11111222223333344444', '448447045')")
    @Test
    public void trackerShouldBeFoundById() {
        super.startQueryCount();
        final TrackerEntity tracker = this.repository.findById(1L).orElseThrow();
        super.checkQueryCount(1);

        assertEquals(1, tracker.getId().longValue());
        assertEquals("11111222223333344444", tracker.getImei());
        assertEquals("448447045", tracker.getPhoneNumber());
    }
}
