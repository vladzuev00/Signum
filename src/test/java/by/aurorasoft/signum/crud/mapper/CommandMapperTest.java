package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.aurorasoft.signum.crud.model.dto.Command.Status.SENT;
import static by.aurorasoft.signum.crud.model.dto.Command.Type.COMMAND;
import static by.aurorasoft.signum.crud.model.dto.Device.Type.TRACKER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class CommandMapperTest extends AbstractContextTest {
    @Autowired
    private CommandMapper commandMapper;

    @Test
    public void entityShouldBeMappedToDto() {
        final CommandEntity givenEntity = CommandEntity.builder()
                .id(255L)
                .text("command")
                .status(SENT)
                .device(DeviceEntity.builder()
                        .id(256L)
                        .imei("11111222223333344444")
                        .phoneNumber("331111111")
                        .type(TRACKER)
                        .build())
                .type(COMMAND)
                .build();

        final Command actual = this.commandMapper.toDto(givenEntity);
        final Command expected = new Command(255L, "command", 256L);

        assertEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Command givenDto = new Command(255L, "command", 25551L);

        final CommandEntity resultEntity = this.commandMapper.toEntity(givenDto);

        assertEquals(255, resultEntity.getId().longValue());
        assertEquals("command", resultEntity.getText());
        assertSame(Command.Status.NOT_DEFINED, resultEntity.getStatus());
        assertEquals(25551, resultEntity.getDevice().getId().longValue());
        assertSame(Command.Type.NOT_DEFINED, resultEntity.getType());
    }
}
