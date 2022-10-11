package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        final Optional<UserEntity> optionalUser = this.repository.findById(1L);
        super.checkQueryCount(1);

        assertTrue(optionalUser.isPresent());
        final UserEntity user = optionalUser.get();
        assertEquals(1, user.getId().longValue());
        assertEquals("vladzuev", user.getName());
        assertTrue(user.getUnits().isEmpty());
    }
}
