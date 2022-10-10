package by.aurorasoft.signum.dtomapper;

import by.aurorasoft.signum.dto.Tracker;
import by.aurorasoft.signum.entity.TrackerEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class TrackerMapper extends AbstractMapper<TrackerEntity, Tracker> {
    public TrackerMapper(ModelMapper modelMapper) {
        super(modelMapper, TrackerEntity.class, Tracker.class);
    }

    @Override
    protected Tracker createDto(TrackerEntity entity) {
        return new Tracker(entity.getId(), entity.getImei(), entity.getPhoneNumber());
    }
}
