package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public final class UserRepositoryTest extends AbstractContextTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void userShouldBeInserted() {
        final UserEntity user = new UserEntity("name", emptyList());
        super.startQueryCount();
        this.repository.save(user);
        super.checkQueryCount(1);
    }

    @Test
    public void userShouldBeFoundById() {
        super.startQueryCount();
        final UserEntity actual = this.repository.findById(25551L).orElseThrow();
        super.checkQueryCount(1);

        final UserEntity expected = UserEntity.builder()
                .id(25551L)
                .name("user_1")
                .units(List.of(super.entityManager.getReference(UnitEntity.class, 25551L)))
                .build();
        checkEquals(expected, actual);
    }

    private static void checkEquals(UserEntity expected, UserEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(new HashSet<>(expected.getUnits()), new HashSet<>(actual.getUnits()));
    }
}
