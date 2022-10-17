package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.mapper.CommandMapper;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.repository.CommandRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public final class CommandService extends AbstractCrudService<Long, CommandEntity, Command> {

    public CommandService(CommandMapper mapper, CommandRepository repository) {
        super(mapper, repository);
    }
}
