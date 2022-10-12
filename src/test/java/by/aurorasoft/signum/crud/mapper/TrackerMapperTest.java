package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Tracker;
import by.aurorasoft.signum.crud.model.entity.TrackerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class TrackerMapperTest extends AbstractContextTest {

    @Autowired
    private TrackerMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Tracker givenTracker = new Tracker(255L, "11111222223333344444", "558447045");

        final TrackerEntity actual = this.mapper.toEntity(givenTracker);
        final TrackerEntity expected = new TrackerEntity(255L, "11111222223333344444", "558447045");
        assertEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final TrackerEntity givenTracker = new TrackerEntity(255L, "11111222223333344444",
                "558447045");

        final Tracker actual = this.mapper.toDto(givenTracker);
        final Tracker expected = new Tracker(255L, "11111222223333344444", "558447045");
        assertEquals(expected, actual);
    }
}
