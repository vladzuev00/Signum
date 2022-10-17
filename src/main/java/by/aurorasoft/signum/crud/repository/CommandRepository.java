package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandRepository extends JpaRepository<CommandEntity, Long> {

}
