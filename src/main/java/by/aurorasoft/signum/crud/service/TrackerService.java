package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.model.dto.Tracker;
import by.aurorasoft.signum.crud.mapper.TrackerMapper;
import by.aurorasoft.signum.crud.model.entity.TrackerEntity;
import by.aurorasoft.signum.crud.repository.TrackerRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public final class TrackerService extends AbstractCrudService<Long, TrackerEntity, Tracker> {
    public TrackerService(TrackerMapper mapper, TrackerRepository repository) {
        super(mapper, repository);
    }
}
