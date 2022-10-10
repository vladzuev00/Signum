package by.aurorasoft.signum.repository;

import by.aurorasoft.signum.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
