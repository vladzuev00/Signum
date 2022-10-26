package by.aurorasoft.signum.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;

import lombok.Value;

@Value
public class Command implements AbstractDto<Long> {
    Long id;
    String text;
    Device device;

    public Command(String text, Device device) {
        this.id = null;
        this.text = text;
        this.device = device;
    }

    public Command(Long id, String text, Device device) {
        this.id = id;
        this.text = text;
        this.device = device;
    }
}
