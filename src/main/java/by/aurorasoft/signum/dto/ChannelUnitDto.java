package by.aurorasoft.signum.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

@Value
public class ChannelUnitDto implements AbstractDto<Long> {
    Long id;
    UserDto user;
    TrackerDto tracker;
}
