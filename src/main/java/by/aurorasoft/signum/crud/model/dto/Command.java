package by.aurorasoft.signum.crud.model.dto;

import by.aurorasoft.signum.crud.model.entity.CommandEntity.Status;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;

import lombok.Value;

@Value
public class Command implements AbstractDto<Long> {
    Long id;
    String text;
    Status status;
    Tracker tracker;
}
