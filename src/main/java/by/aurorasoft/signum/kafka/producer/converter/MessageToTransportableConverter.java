package by.aurorasoft.signum.kafka.producer.converter;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.kafka.producer.transportable.TransportableMessage;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;

@Component
public final class MessageToTransportableConverter {
    private static final Long NOT_DEFINED_ID = 0L;
    private static final long NOT_DEFINED_DATE_TIME_EPOCH_SECOND = 0;
    private static final float NOT_DEFINED_LATITUDE = 0;
    private static final float NOT_DEFINED_LONGITUDE = 0;

    public TransportableMessage convert(Message converted) {
        return new TransportableMessage(
                findId(converted),
                findDataTimeEpochSecond(converted),
                findLatitude(converted),
                findLongitude(converted),
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



    private static Long findId(Message message) {
        return message.getId() != null ? message.getId() : NOT_DEFINED_ID;
    }

    private static long findDataTimeEpochSecond(Message message) {
        return message.getDatetime() != null ? message.getDatetime().getEpochSecond() : NOT_DEFINED_DATE_TIME_EPOCH_SECOND;
    }

    private static float findLatitude(Message message) {
        return message.getCoordinate() != null ? message.getCoordinate().getLatitude() : NOT_DEFINED_LATITUDE;
    }

    private static float findLongitude(Message message) {
        return message.getCoordinate() != null ? message.getCoordinate().getLongitude() : NOT_DEFINED_LONGITUDE;
    }
}
