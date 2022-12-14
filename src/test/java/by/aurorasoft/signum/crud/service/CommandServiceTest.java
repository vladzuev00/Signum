package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Command.Status;
import by.aurorasoft.signum.crud.model.dto.Command.Type;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

        final CommandEntity actual = super.findEntityFromDB(CommandEntity.class, savedCommand.getId());
        final CommandEntity expected = CommandEntity.builder()
                .id(savedCommand.getId())
                .text("command")
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .status(SENT)
                .type(COMMAND)
                .build();
        checkEquals(expected, actual);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void commandWithNotDefinedStatusShouldNotBeSaved() {
        final Command givenCommand = new Command("command", 25551L);
        this.service.save(givenCommand, Status.NOT_DEFINED, COMMAND);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void commandWithNotDefinedTypeShouldNotBeSaved() {
        final Command givenCommand = new Command("command", 25551L);
        this.service.save(givenCommand, SENT, Type.NOT_DEFINED);
    }

    @Test
    @Sql(statements = "INSERT INTO command(id, text, status, device_id, type) VALUES(255, 'command', 'NEW', 25551, 'COMMAND')")
    public void commandShouldBeUpdatedByStatus() {
        final Command command = new Command(255L, "command", 25551L);
        this.service.updateStatus(command, SENT);

        final CommandEntity actual = super.findEntityFromDB(CommandEntity.class, 255L);
        final CommandEntity expected = CommandEntity.builder()
                .id(255L)
                .text("command")
                .status(SENT)
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .type(COMMAND)
                .build();
        checkEquals(expected, actual);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void commandShouldNotBeUpdatedByNotDefinedStatus() {
        final Command command = new Command(255L, "command", 25551L);
        this.service.updateStatus(command, Status.NOT_DEFINED);
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

    @Test(expected = DataIntegrityViolationException.class)
    public void commandsShouldNotBeFoundByNotDefinedStatus() {
        final Device givenDevice = new Device(25552L, "355026070842667", "+3197011460885", TRACKER);
        this.service.findCommandsByDeviceAndStatuses(givenDevice, Status.NOT_DEFINED, SUCCESS);
    }

    private static void checkEquals(CommandEntity expected, CommandEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getText(), actual.getText());
        assertSame(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getDevice().getId(), actual.getDevice().getId());
        assertSame(expected.getType(), actual.getType());
    }
}
