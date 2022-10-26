package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.function.ToLongFunction;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.*;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Type.COMMAND;
import static by.aurorasoft.signum.crud.model.entity.DeviceEntity.Type.TRACKER;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public final class CommandServiceTest extends AbstractContextTest {
    private static final ToLongFunction<CommandEntity> COMMAND_TO_DEVICE_ID_FUNCTION
            = command -> command.getDevice().getId();

    @Autowired
    private CommandService service;

    @Test
    public void commandShouldBeSaved() {
        final Command givenCommand = new Command("command",
                new Device(25551L, "355234055650192", "+37257063997", TRACKER));

        final Command savedCommand = this.service.save(givenCommand, SENT, COMMAND);
        final CommandEntity savedCommandEntity = super.findEntityFromDB(CommandEntity.class, savedCommand.getId());

        assertEquals("command", savedCommandEntity.getText());
        assertEquals(25551L, COMMAND_TO_DEVICE_ID_FUNCTION.applyAsLong(savedCommandEntity));
        assertSame(SENT, savedCommandEntity.getStatus());
        assertSame(COMMAND, savedCommandEntity.getType());
    }

    @Test
    @Sql(statements
            = {
            "INSERT INTO command(id, text, status, device_id, type) VALUES(255, 'command', 'NEW', 25551, 'COMMAND')",
            "INSERT INTO command(id, text, status, device_id, type) VALUES(256, 'command', 'SENT', 25552, 'COMMAND')",
            "INSERT INTO command(id, text, status, device_id, type) VALUES(257, 'command', 'SUCCESS', 25552, 'ANSWER')"
    })
    public void commandsShouldBeFoundByStatuses() {
        final List<Command> foundCommands = this.service.findCommandsByStatuses(SENT, NEW);
        final List<Long> actual = foundCommands.stream()
                .map(Command::getId)
                .collect(toList());
        final List<Long> expected = List.of(255L, 256L);
        assertEquals(expected, actual);
    }

    @Test
    public void commandsShouldNotBeFoundByStatuses() {
        final List<Command> foundCommands = this.service.findCommandsByStatuses(NOT_DEFINED);
        assertTrue(foundCommands.isEmpty());
    }
}
