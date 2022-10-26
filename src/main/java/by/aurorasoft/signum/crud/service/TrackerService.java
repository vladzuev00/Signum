package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.mapper.TrackerMapper;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.repository.DeviceRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public final class TrackerService extends AbstractCrudService<Long, DeviceEntity, Device> {
    public TrackerService(TrackerMapper mapper, DeviceRepository repository) {
        super(mapper, repository);
    }
}
