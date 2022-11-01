package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.model.dto.User;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class UnitMapper extends AbsMapperEntityDto<UnitEntity, Unit> {

    public UnitMapper(ModelMapper modelMapper) {
        super(modelMapper, UnitEntity.class, Unit.class);
    }

    @Override
    protected Unit create(UnitEntity entity) {
        final User user = map(entity.getUser(), User.class);
        return new Unit(entity.getId(), entity.getName(), user);
    }
}
