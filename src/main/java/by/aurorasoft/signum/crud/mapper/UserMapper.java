package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.User;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class UserMapper extends AbstractMapper<UserEntity, User> {

    public UserMapper(ModelMapper modelMapper) {
        super(modelMapper, UserEntity.class, User.class);
    }

    @Override
    protected User createDto(UserEntity entity) {
        return new User(entity.getId(), entity.getName());
    }
}
