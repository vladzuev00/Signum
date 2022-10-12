package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Tracker;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.model.dto.User;
import by.aurorasoft.signum.crud.model.entity.TrackerEntity;
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
        final UnitEntity givenEntity = new UnitEntity(255L, "unit_a",
                new UserEntity(256L, "user_a", emptyList()),
                new TrackerEntity(257L, "11111222223333344444", "558447045"));

        final Unit actual = this.mapper.toDto(givenEntity);
        final Unit expected = new Unit(255L, "unit_a",
                new User(256L, "user_a"),
                new Tracker(257L, "11111222223333344444", "558447045"));
        assertEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Unit givenUnit = new Unit(255L, "unit_a",
                new User(256L, "user_1"),
                new Tracker(257L, "355234055650192", "+37257063997"));

        final UnitEntity actual = this.mapper.toEntity(givenUnit);
        final UnitEntity expected = new UnitEntity(255L, "unit_a",
                new UserEntity(256L, "user_1", null),
                new TrackerEntity(257L, "355234055650192", "+37257063997"));
        assertEquals(expected, actual);
    }
}
