package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.mapper.UnitMapper;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.aurorasoft.signum.crud.repository.UnitRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnitService extends AbstractCrudService<Long, UnitEntity, Unit> {

    public UnitService(UnitMapper mapper, UnitRepository repository) {
        super(mapper, repository);
    }
}
