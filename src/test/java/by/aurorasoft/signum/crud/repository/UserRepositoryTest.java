package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        final UserEntity user = this.repository.findById(25551L).orElseThrow();
        super.checkQueryCount(1);

        assertEquals(25551, user.getId().longValue());
        assertEquals("user_1", user.getName());

        final List<UnitEntity> units = user.getUnits();
        assertEquals(1, units.size());
        assertEquals(25551, units.get(0).getId().longValue());
    }
}
