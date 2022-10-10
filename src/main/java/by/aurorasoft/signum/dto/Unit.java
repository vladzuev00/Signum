package by.aurorasoft.signum.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

@Value
public class Unit implements AbstractDto<Long> {
    Long id;
    User user;
    Tracker tracker;
}
