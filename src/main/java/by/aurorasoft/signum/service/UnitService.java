package by.aurorasoft.signum.service;

import by.aurorasoft.signum.dto.Unit;
import by.aurorasoft.signum.dtomapper.UnitMapper;
import by.aurorasoft.signum.entity.UnitEntity;
import by.aurorasoft.signum.repository.UnitRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public final class UnitService extends AbstractCrudService<Long, UnitEntity, Unit> {

    public UnitService(UnitMapper mapper, UnitRepository repository) {
        super(mapper, repository);
    }
}
