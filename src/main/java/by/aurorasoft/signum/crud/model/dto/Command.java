package by.aurorasoft.signum.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;

import lombok.Value;

@Value
public class Command implements AbstractDto<Long> {
    Long id;
    String text;
    Tracker tracker;
}
