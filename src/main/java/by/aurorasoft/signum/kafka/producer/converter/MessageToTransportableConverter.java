package by.aurorasoft.signum.kafka.producer.converter;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.kafka.producer.transportable.TransportableMessage;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;

@Component
public final class MessageToTransportableConverter {

    public TransportableMessage convert(Message converted) {
        return new TransportableMessage(
                converted.getId(),
                converted.getDatetime().getEpochSecond(),
                converted.getCoordinate().getLatitude(),
                converted.getCoordinate().getLongitude(),
                converted.getAltitude(),
                converted.getSpeed(),
                converted.getAmountSatellite(),
                converted.getCourse(),
                converted.getParameter(GSM_LEVEL),
                converted.getParameter(VOLTAGE),
                converted.getParameter(CORNER_ACCELERATION),
                converted.getParameter(ACCELERATION_UP),
                converted.getParameter(ACCELERATION_DOWN),
                converted.getType(),
                converted.getGpsOdometer(),
                converted.getIgnition(),
                converted.getEngineTime(),
                converted.getShock(),
                converted.getDeviceId());
    }
}
