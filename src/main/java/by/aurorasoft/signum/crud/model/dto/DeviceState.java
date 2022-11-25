package by.aurorasoft.signum.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

@Value
public class DeviceState implements AbstractDto<Long> {
    Device device;
    Message lastMessage;

    @Override
    public Long getId() {
        return this.device.getId();
    }
}
