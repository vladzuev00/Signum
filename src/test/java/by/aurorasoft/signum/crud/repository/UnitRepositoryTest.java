package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class UnitRepositoryTest extends AbstractContextTest {

    @Autowired
    private UnitRepository unitRepository;

    @Test
    public void unitShouldBeInserted() {
//        final UnitEntity givenUnit = new UnitEntity(
//                "unit-name",
//                super.entityManager.getReference(UserEntity.class, 25551L),
//                super.entityManager.getReference(DeviceEntity.class, 25555L)
//        );

//        super.startQueryCount();
//        this.unitRepository.save(givenUnit);
//        super.checkQueryCount(1);
    }

    @Test
    public void unitShouldBeFoundById() {
        super.startQueryCount();
        final UnitEntity unit = this.unitRepository.findById(25551L).orElseThrow();
        super.checkQueryCount(1);

        assertEquals(25551, unit.getId().longValue());
        assertEquals("unit_a", unit.getName());
        assertEquals(25551, unit.getUser().getId().longValue());
//        assertEquals(25551, unit.getDevice().getId().longValue());
    }

    @Test
    @SuppressWarnings("all")
    public void unitShouldBeFoundByTrackerImeiAndUserAndTrackerShouldBeLoadedEagerly() {
//        super.startQueryCount();
//        final UnitEntity unit = this.unitRepository.findByDevice_imei("355234055650192").orElseThrow();
//        unit.getUser().getName();
////        unit.getDevice().getImei();
//        super.checkQueryCount(1);
//
//        assertEquals(25551, unit.getId().longValue());
    }

    @Test
    public void unitShouldNotBeFoundByTrackerImei() {
//        final Optional<UnitEntity> optionalUnit = this.unitRepository.findByDevice_imei("00000000000000000000");
//        assertTrue(optionalUnit.isEmpty());
    }
}
