package by.aurorasoft.signum.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

@Value
public class User implements AbstractDto<Long> {
    Long id;
    String name;
}
