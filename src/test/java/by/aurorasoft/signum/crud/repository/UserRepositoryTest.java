package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

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

    @Sql(statements = "INSERT INTO app_user(id, name) VALUES(1, 'vladzuev')")
    @Test
    public void userShouldBeFoundById() {
        super.startQueryCount();
        final UserEntity user = this.repository.findById(1L).orElseThrow();
        super.checkQueryCount(1);

        assertEquals(1, user.getId().longValue());
        assertEquals("vladzuev", user.getName());
        assertTrue(user.getUnits().isEmpty());
    }
}
