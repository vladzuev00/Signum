package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Command.Status;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;

import static by.aurorasoft.signum.crud.model.dto.Command.Status.*;
import static by.aurorasoft.signum.crud.model.dto.Command.Type.COMMAND;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.builder;
import static java.lang.Long.MIN_VALUE;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

public final class CommandRepositoryTest extends AbstractContextTest {

    @Autowired
    private CommandRepository repository;

    @Test
    public void commandShouldBeInserted() {
        final CommandEntity givenCommand = builder()
                .text("command")
                .status(NEW)
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .type(COMMAND)
                .build();
        super.startQueryCount();
        this.repository.save(givenCommand);
        super.checkQueryCount(1);
    }

    @Test
    @Sql(statements
            = "INSERT INTO command(id, text, status, device_id, type) VALUES(255, 'command', 'NEW', 25551, 'COMMAND')")
    public void commandShouldBeFoundById() {
        super.startQueryCount();
        final CommandEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final CommandEntity expected = CommandEntity.builder()
                .id(255L)
                .text("command")
                .status(NEW)
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .type(COMMAND)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql(statements
            = "INSERT INTO command(id, text, status, device_id, type) VALUES(255, 'command', 'NEW', 25551, 'COMMAND')")
    public void commandShouldBeUpdatedByStatus() {
        super.startQueryCount();
        this.repository.updateStatus(255L, SENT);
        super.checkQueryCount(1);

        final CommandEntity actual = this.repository.findById(255L).orElseThrow();
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
        this.repository.updateStatus(255L, Status.NOT_DEFINED);
    }

    @Test
    @Sql(statements
            = {
            "INSERT INTO command(id, text, status, device_id, type) VALUES(255, 'command', 'NEW', 25551, 'COMMAND')",
            "INSERT INTO command(id, text, status, device_id, type) VALUES(256, 'command', 'SENT', 25552, 'COMMAND')",
            "INSERT INTO command(id, text, status, device_id, type) VALUES(257, 'command', 'SUCCESS', 25552, 'ANSWER')"
    })
    public void commandsShouldBeFoundByStatuses() {
        super.startQueryCount();
        final List<CommandEntity> foundCommands = this.repository
                .findByDeviceIdAndStatuses(25552L, Set.of(SENT, SUCCESS));
        super.checkQueryCount(1);

        final Set<Long> actual = foundCommands.stream()
                .map(CommandEntity::getId)
                .collect(toSet());
        final Set<Long> expected = Set.of(256L, 257L);
        assertEquals(expected, actual);
    }

    @Test
    public void commandsShouldNotBeFoundByStatuses() {
        super.startQueryCount();
        final List<CommandEntity> foundCommands = this.repository
                .findByDeviceIdAndStatuses(MIN_VALUE, Set.of(SUCCESS));
        super.checkQueryCount(1);

        assertTrue(foundCommands.isEmpty());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void commandsShouldNotBeFoundByNotDefinedStatus() {
        this.repository.findByDeviceIdAndStatuses(MIN_VALUE, Set.of(Status.NOT_DEFINED, SUCCESS));
    }

    private static void checkEquals(CommandEntity expected, CommandEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getText(), actual.getText());
        assertSame(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getDevice().getId(), actual.getDevice().getId());
        assertSame(expected.getType(), actual.getType());
    }
}
