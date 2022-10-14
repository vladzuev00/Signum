package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.TrackerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class TrackerRepositoryTest extends AbstractContextTest {
    @Autowired
    private TrackerRepository repository;

    @Test
    public void trackerShouldBeInserted() {
        final TrackerEntity tracker = new TrackerEntity(null, "11111222223333344444", "448447045");
        super.startQueryCount();
        this.repository.save(tracker);
        super.checkQueryCount(1);
    }

    @Test
    public void trackerShouldBeFoundById() {
        super.startQueryCount();
        final TrackerEntity tracker = this.repository.findById(25551L).orElseThrow();
        super.checkQueryCount(1);

        assertEquals(25551, tracker.getId().longValue());
        assertEquals("355234055650192", tracker.getImei());
        assertEquals("+37257063997", tracker.getPhoneNumber());
    }
}
