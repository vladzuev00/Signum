package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.User;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public final class UserMapperTest extends AbstractContextTest {

    @Autowired
    private UserMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final User givenUser = new User(255L, "user_a");

        final UserEntity actual = this.mapper.toEntity(givenUser);
        final UserEntity expected = new UserEntity(255L, "user_a", null);
        assertEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final UserEntity givenUser = new UserEntity(255L, "user_a", emptyList());

        final User actual = this.mapper.toDto(givenUser);
        final User expected = new User(255L, "user_a");
        assertEquals(expected, actual);
    }
}
