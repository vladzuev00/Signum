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
        final UnitEntity givenUnit = UnitEntity.builder()
                .name("unit-name")
                .user(super.entityManager.getReference(UserEntity.class, 25551L))
                .build();

        super.startQueryCount();
        this.unitRepository.save(givenUnit);
        super.checkQueryCount(1);
    }

    @Test
    public void unitShouldBeFoundById() {
        super.startQueryCount();
        final UnitEntity actual = this.unitRepository.findById(25551L).orElseThrow();
        super.checkQueryCount(1);

        final UnitEntity expected = UnitEntity.builder()
                .id(25551L)
                .name("unit_a")
                .user(super.entityManager.getReference(UserEntity.class, 25551L))
                .build();
        checkEquals(expected, actual);
    }

    private static void checkEquals(UnitEntity expected, UnitEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getUser().getId(), actual.getUser().getId());
    }
}
