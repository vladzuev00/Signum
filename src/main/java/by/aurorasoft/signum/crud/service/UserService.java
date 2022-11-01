package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.mapper.UserMapper;
import by.aurorasoft.signum.crud.model.dto.User;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import by.aurorasoft.signum.crud.repository.UserRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService extends AbsServiceCRUD<Long, UserEntity, User, UserRepository> {

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        super(userMapper, userRepository);
    }
}
