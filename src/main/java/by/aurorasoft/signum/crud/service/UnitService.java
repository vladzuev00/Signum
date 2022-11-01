package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.mapper.UnitMapper;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.aurorasoft.signum.crud.repository.UnitRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UnitService extends AbsServiceCRUD<Long, UnitEntity, Unit, UnitRepository> {

    public UnitService(UnitMapper mapper, UnitRepository repository) {
        super(mapper, repository);
    }
}
