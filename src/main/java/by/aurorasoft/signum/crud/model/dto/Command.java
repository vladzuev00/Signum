package by.aurorasoft.signum.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;

import lombok.Value;

@Value
public class Command implements AbstractDto<Long> {
    Long id;
    String text;
    Long deviceId;

    public Command(String text, Long deviceId) {
        this.id = null;
        this.text = text;
        this.deviceId = deviceId;
    }

    public Command(Long id, String text, Long deviceId) {
        this.id = id;
        this.text = text;
        this.deviceId = deviceId;
    }
}
