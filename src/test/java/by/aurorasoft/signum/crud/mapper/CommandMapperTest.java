package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Command.Status;
import by.aurorasoft.signum.crud.model.dto.Command.Type;
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
    public void entityWithNullAsDeviceShouldBeMappedToDtoWithDeviceIdEqualsZero() {
        final CommandEntity givenEntity = CommandEntity.builder()
                .id(255L)
                .text("command")
                .status(SENT)
                .type(COMMAND)
                .build();

        final Command actual = this.commandMapper.toDto(givenEntity);
        final Command expected = new Command(255L, "command", 0L);

        assertEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Command givenDto = new Command(255L, "command", 25551L);

        final CommandEntity actual = this.commandMapper.toEntity(givenDto);
        final CommandEntity expected = CommandEntity.builder()
                .id(255L)
                .text("command")
                .status(Status.NOT_DEFINED)
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .type(Type.NOT_DEFINED)
                .build();

        checkEquals(expected, actual);
    }

    private static void checkEquals(CommandEntity expected, CommandEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getText(), actual.getText());
        assertSame(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getDevice().getId(), actual.getDevice().getId());
        assertSame(expected.getType(), actual.getType());
    }
}
