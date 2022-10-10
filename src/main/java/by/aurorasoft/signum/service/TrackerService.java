package by.aurorasoft.signum.service;

import by.aurorasoft.signum.dto.Tracker;
import by.aurorasoft.signum.dtomapper.TrackerMapper;
import by.aurorasoft.signum.entity.TrackerEntity;
import by.aurorasoft.signum.repository.TrackerRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public final class TrackerService extends AbstractCrudService<Long, TrackerEntity, Tracker> {
    public TrackerService(TrackerMapper mapper, TrackerRepository repository) {
        super(mapper, repository);
    }
}
