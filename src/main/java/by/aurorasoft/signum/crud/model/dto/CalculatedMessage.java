package by.aurorasoft.signum.crud.model.dto;

import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

@Value
public class CalculatedMessage implements AbstractDto<Long> {
    Message inboundMessage;
    MessageType type;
    double gpsOdometer;

    @Override
    public Long getId() {
        return this.inboundMessage.getId();
    }
}
