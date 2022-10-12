package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;

public final class MessageMapperTest extends AbstractContextTest {

    @Autowired
    private MessageMapper mapper;

    @Test
    public void entityShouldBeMappedToDto() {
        final MessageEntity givenEntity = new MessageEntity(255L, new UnitEntity(),
                parse("2000-02-18T04:05:06Z"), 4.4F, 5.5F, 10, 11, 12,
                13, 14, "param1:value1,param2:value2");

        final Message actual = this.mapper.toDto(givenEntity);
        final Message expected = new Message(255L, parse("2000-02-18T04:05:06Z"),
                new GpsCoordinate(4.4F, 5.5F), 10, 11, 12, 13, 14,
                "param1:value1,param2:value2");

        assertEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/insert-message.sql")
    public void dtoShouldBeMappedToEntity() {
        final Message givenDto = new Message(1L, parse("2000-02-18T04:05:06Z"),
                new GpsCoordinate(4.4F, 5.5F), 10, 11, 12, 13,
                7.7F, "param1:value1,param2:value2");

        final MessageEntity actual = this.mapper.toEntity(givenDto);
        final MessageEntity expected = new MessageEntity(1L,
                super.entityManager.getReference(UnitEntity.class, 1L),
                parse("2000-02-18T04:05:06Z"), 4.4F, 5.5F, 10, 11, 12,
                13, 6.6F, "param1:value1,param2:value2");
        assertEquals(expected, actual);
    }
}
