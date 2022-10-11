package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Tracker;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.model.dto.User;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class UnitMapper extends AbstractMapper<UnitEntity, Unit> {

    public UnitMapper(ModelMapper modelMapper) {
        super(modelMapper, UnitEntity.class, Unit.class);
    }

    @Override
    protected Unit createDto(UnitEntity entity) {
        final User user = super.modelMapper.map(entity.getUser(), User.class);
        final Tracker tracker = super.modelMapper.map(entity.getTracker(), Tracker.class);
        return new Unit(entity.getId(), entity.getName(), user, tracker);
    }
}
