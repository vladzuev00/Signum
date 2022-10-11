package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
