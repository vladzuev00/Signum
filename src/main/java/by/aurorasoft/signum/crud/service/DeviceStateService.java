package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.mapper.DeviceStateMapper;
import by.aurorasoft.signum.crud.model.dto.DeviceState;
import by.aurorasoft.signum.crud.model.entity.DeviceStateEntity;
import by.aurorasoft.signum.crud.repository.DeviceStateRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DeviceStateService
        extends AbsServiceCRUD<Long, DeviceStateEntity, DeviceState, DeviceStateRepository> {

    public DeviceStateService(DeviceStateMapper mapper, DeviceStateRepository repository) {
        super(mapper, repository);
    }
}
