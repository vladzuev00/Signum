package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class UnitRepositoryTest extends AbstractContextTest {

    @Autowired
    private UnitRepository unitRepository;

    @Test
    public void unitShouldBeInserted() {
        final UnitEntity givenUnit = new UnitEntity(
                "unit-name",
                super.entityManager.getReference(UserEntity.class, 25551L)
        );

        super.startQueryCount();
        this.unitRepository.save(givenUnit);
        super.checkQueryCount(1);
    }

    @Test
    public void unitShouldBeFoundById() {
        super.startQueryCount();
        final UnitEntity unit = this.unitRepository.findById(25551L).orElseThrow();
        super.checkQueryCount(1);

        assertEquals(25551, unit.getId().longValue());
        assertEquals("unit_a", unit.getName());
        assertEquals(25551, unit.getUser().getId().longValue());
    }
}
