package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.TrackerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.SUCCESS_DELIVERED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class CommandRepositoryTest extends AbstractContextTest {

    @Autowired
    private CommandRepository repository;

    @Test
    public void commandShouldBeInserted() {
        final CommandEntity givenCommand = new CommandEntity(null, "command", SUCCESS_DELIVERED,
                super.entityManager.getReference(TrackerEntity.class, 25551L));
        super.startQueryCount();
        this.repository.save(givenCommand);
        super.checkQueryCount(1);
    }

    @Test
    @Sql(statements = "INSERT INTO command(id, text, status, tracker_id) VALUES(255, 'command', 'DELIVERED', 25551)")
    public void commandShouldBeFoundById() {
        super.startQueryCount();
        final CommandEntity foundCommand = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        assertEquals(255, foundCommand.getId().longValue());
        assertEquals("command", foundCommand.getText());
        assertSame(SUCCESS_DELIVERED, foundCommand.getStatus());
        assertEquals(25551, foundCommand.getTracker().getId().longValue());
    }
}
