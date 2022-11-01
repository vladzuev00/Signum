package by.aurorasoft.signum.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

@Value
public class Device implements AbstractDto<Long> {
    Long id;
    String imei;
    String phoneNumber;
    Type type;

    public enum Type {
        NOT_DEFINED, TRACKER, BEACON
    }
}
