package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;
import java.util.function.ToLongFunction;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.*;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Type.COMMAND;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.builder;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public final class CommandRepositoryTest extends AbstractContextTest {
    private static final ToLongFunction<CommandEntity> COMMAND_TO_DEVICE_ID_FUNCTION
            = command -> command.getDevice().getId();

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
        final CommandEntity foundCommand = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        assertEquals(255, foundCommand.getId().longValue());
        assertEquals("command", foundCommand.getText());
        assertSame(NEW, foundCommand.getStatus());
        assertEquals(25551, COMMAND_TO_DEVICE_ID_FUNCTION.applyAsLong(foundCommand));
        assertSame(COMMAND, foundCommand.getType());
    }

    @Test
    @Sql(statements
            = "INSERT INTO command(id, text, status, device_id, type) VALUES(255, 'command', 'NEW', 25551, 'COMMAND')")
    public void commandShouldBeUpdatedByStatus() {
        super.startQueryCount();
        this.repository.updateByStatus(255L, SENT);
        super.checkQueryCount(1);

        final CommandEntity updatedCommand = this.repository.findById(255L).orElseThrow();
        assertEquals(255, updatedCommand.getId().longValue());
        assertEquals("command", updatedCommand.getText());
        assertSame(SENT, updatedCommand.getStatus());
        assertEquals(25551, COMMAND_TO_DEVICE_ID_FUNCTION.applyAsLong(updatedCommand));
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
        super.startQueryCount();
        final List<CommandEntity> foundCommands = this.repository.findByStatuses(Set.of(NEW, SENT));
        super.checkQueryCount(1);

        final List<Long> actual = foundCommands.stream()
                .map(CommandEntity::getId)
                .collect(toList());
        final List<Long> expected = List.of(255L, 256L);
        assertEquals(expected, actual);
    }

    @Test
    public void commandsShouldNotBeFoundByStatuses() {
        super.startQueryCount();
        final List<CommandEntity> foundCommands = this.repository.findByStatuses(Set.of(NOT_DEFINED));
        super.checkQueryCount(1);

        assertTrue(foundCommands.isEmpty());
    }
}
