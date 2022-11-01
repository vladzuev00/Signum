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

    public enum Status {
        NOT_DEFINED, NEW, SENT, SUCCESS, ERROR, TIMEOUT
    }

    public enum Type {

        NOT_DEFINED,

        /**
         * indicates that command has been delivered by server to device
         */
        COMMAND,

        /**
         * indicates that command has been delivered by device to server
         */
        ANSWER
    }
}
