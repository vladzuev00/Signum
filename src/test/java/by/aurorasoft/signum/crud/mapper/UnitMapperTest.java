package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.model.dto.User;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public final class UnitMapperTest extends AbstractContextTest {

    @Autowired
    private UnitMapper mapper;

    @Test
    public void entityShouldBeMappedToDto() {
        final UnitEntity givenEntity = UnitEntity.builder()
                .id(255L)
                .name("unit_a")
                .user(new UserEntity(256L, "user_a", emptyList()))
                .build();

        final Unit actual = this.mapper.toDto(givenEntity);
        final Unit expected = new Unit(255L, "unit_a", new User(256L, "user_a"));

        assertEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Unit givenUnit = new Unit(255L, "unit_a", new User(256L, "user_1"));

        final UnitEntity actual = this.mapper.toEntity(givenUnit);
        final UnitEntity expected = UnitEntity.builder()
                .id(255L)
                .name("unit_a")
                .user(super.entityManager.getReference(UserEntity.class, 256L))
                .build();

        checkEquals(expected, actual);
    }

    private static void checkEquals(UnitEntity expected, UnitEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getUser().getId(), actual.getUser().getId());
    }
}
