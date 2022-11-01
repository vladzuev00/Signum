package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.model.dto.User;
import by.aurorasoft.signum.crud.model.entity.UserEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbstractMapper;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService extends AbstractCrudService<Long, UserEntity, User> {

    public UserService(AbstractMapper<UserEntity, User> mapper,
                       JpaRepository<UserEntity, Long> repository) {
        super(mapper, repository);
    }
}
