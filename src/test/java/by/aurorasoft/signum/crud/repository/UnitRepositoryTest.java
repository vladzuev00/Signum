package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.TrackerEntity;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class UnitRepositoryTest extends AbstractContextTest {

    @Autowired
    private UnitRepository unitRepository;

    @Sql(statements = "INSERT INTO app_user(id, name) VALUES(1, 'vladzuev')")
    @Sql(statements = "INSERT INTO tracker(id, imei, phone_number) VALUES(1, '11111222223333344444', '448447045')")
    @Test
    public void unitShouldBeInserted() {
        final UnitEntity givenUnit = new UnitEntity(
                "unit-name",
                createEntity(1L, UserEntity.class),
                createEntity(1L, TrackerEntity.class));

        super.startQueryCount();
        this.unitRepository.save(givenUnit);
        super.checkQueryCount(1);
    }

    @Sql("classpath:sql/insert-unit.sql")
    @Test
    public void unitShouldBeFoundById() {
        super.startQueryCount();
        final Optional<UnitEntity> optionalUnit = this.unitRepository.findById(1L);
        super.checkQueryCount(1);

        assertTrue(optionalUnit.isPresent());
        final UnitEntity unit = optionalUnit.get();

        assertEquals(1, unit.getId().longValue());
        assertEquals("unit-name", unit.getName());
        assertEquals(1, unit.getUser().getId().longValue());
        assertEquals(1, unit.getTracker().getId().longValue());
    }
}
