package by.aurorasoft.signum.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Device implements AbstractDto<Long> {
    Long id;
    String imei;
    String phoneNumber;
    Type type;

    public enum Type {
        NOT_DEFINED, TRACKER, BEACON
    }
}
