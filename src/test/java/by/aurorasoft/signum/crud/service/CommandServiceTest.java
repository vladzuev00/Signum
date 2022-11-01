package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static by.aurorasoft.signum.crud.model.dto.Command.Status.*;
import static by.aurorasoft.signum.crud.model.dto.Command.Type.COMMAND;
import static by.aurorasoft.signum.crud.model.dto.Device.Type.TRACKER;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public final class CommandServiceTest extends AbstractContextTest {

    @Autowired
    private CommandService service;

    @Test
    public void commandShouldBeSaved() {
        final Command givenCommand = new Command("command", 25551L);

        final Command savedCommand = this.service.save(givenCommand, SENT, COMMAND);
        final CommandEntity savedCommandEntity = super.findEntityFromDB(CommandEntity.class, savedCommand.getId());

        assertEquals("command", savedCommandEntity.getText());
        assertEquals(25551, savedCommandEntity.getDevice().getId().longValue());
        assertSame(SENT, savedCommandEntity.getStatus());
        assertSame(COMMAND, savedCommandEntity.getType());
    }

    @Test
    @Sql(statements = "INSERT INTO command(id, text, status, device_id, type) VALUES(255, 'command', 'NEW', 25551, 'COMMAND')")
    public void commandShouldBeUpdatedByStatus() {
        final Command command = new Command(255L, "command", 25551L);
        this.service.updateStatus(command, SENT);

        final CommandEntity updatedCommand = super.findEntityFromDB(CommandEntity.class, 255L);
        assertEquals("command", updatedCommand.getText());
        assertSame(SENT, updatedCommand.getStatus());
        assertEquals(25551, updatedCommand.getDevice().getId().longValue());
        assertSame(COMMAND, updatedCommand.getType());
    }

    @Test
    @Sql(statements
            = {
            "INSERT INTO command(id, text, status, device_id, type) VALUES(255, 'command', 'NEW', 25551, 'COMMAND')",
            "INSERT INTO command(id, text, status, device_id, type) VALUES(256, 'command', 'SENT', 25552, 'COMMAND')",
            "INSERT INTO command(id, text, status, device_id, type) VALUES(257, 'command', 'SUCCESS', 25552, 'ANSWER')"
    })
    public void commandsShouldBeFoundByStatuses() {
        final Device givenDevice = new Device(25552L, "355026070842667", "+3197011460885", TRACKER);
        final List<Command> foundCommands = this.service.findCommandsByDeviceAndStatuses(givenDevice, SENT, SUCCESS);
        final List<Long> actual = foundCommands.stream()
                .map(Command::getId)
                .collect(toList());
        final List<Long> expected = List.of(256L, 257L);
        assertEquals(expected, actual);
    }

    @Test
    public void commandsShouldNotBeFoundByStatuses() {
        final Device givenDevice = new Device(25552L, "355026070842667", "+3197011460885", TRACKER);
        final List<Command> foundCommands = this.service.findCommandsByDeviceAndStatuses(givenDevice, SUCCESS);
        assertTrue(foundCommands.isEmpty());
    }
}
