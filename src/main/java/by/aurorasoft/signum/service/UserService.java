package by.aurorasoft.signum.service;

import by.aurorasoft.signum.dto.User;
import by.aurorasoft.signum.entity.UserEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbstractMapper;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public final class UserService extends AbstractCrudService<Long, UserEntity, User> {

    public UserService(AbstractMapper<UserEntity, User> mapper,
                       JpaRepository<UserEntity, Long> repository) {
        super(mapper, repository);
    }
}
