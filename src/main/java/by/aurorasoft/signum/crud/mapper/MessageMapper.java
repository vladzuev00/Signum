package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityExtDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;

@Component
public final class MessageMapper extends AbsMapperEntityExtDto<MessageEntity, Message, Long, DeviceEntity> {
    public MessageMapper(ModelMapper modelMapper, EntityManager entityManager) {
        super(modelMapper, MessageEntity.class, Message.class, entityManager, DeviceEntity.class);
    }

    @Override
    protected void mapSpecificFields(Message source, MessageEntity destination) {
        mapGpsCoordinate(source, destination);
        mapParameters(source, destination);
    }

    @Override
    protected void setRelation(DeviceEntity device, MessageEntity message) {
        message.setDevice(device);
    }

    @Override
    protected Message create(MessageEntity entity) {
        return new Message(
                entity.getId(),
                entity.getDatetime(),
                new GpsCoordinate(entity.getLatitude(), entity.getLongitude()),
                entity.getSpeed(),
                entity.getCourse(),
                entity.getAltitude(),
                entity.getAmountSatellite(),
                mapParameters(entity));
    }

    private static void mapGpsCoordinate(Message source, MessageEntity destination) {
        final GpsCoordinate gpsCoordinate = source.getCoordinate();
        destination.setLatitude(gpsCoordinate.getLatitude());
        destination.setLongitude(gpsCoordinate.getLongitude());
    }

    private static void mapParameters(Message source, MessageEntity destination) {
        destination.setGsmLevel(source.getParameter(GSM_LEVEL).intValue());
        destination.setOnboardVoltage(source.getParameter(VOLTAGE).floatValue());
        destination.setEcoCornering(source.getParameter(CORNER_ACCELERATION).floatValue());
        destination.setEcoAcceleration(source.getParameter(ACCELERATION_UP).intValue());
        destination.setEcoBraking(source.getParameter(ACCELERATION_DOWN).intValue());
        destination.setGpsOdometer(source.getParameter(GPS_ODOMETER));
        destination.setIgnition(source.getParameter(IGNITION).intValue());
        destination.setEngineTime(source.getParameter(ENGINE_TIME).intValue());
    }

    private static Map<ParameterName, Double> mapParameters(MessageEntity entity) {
        return Map.of(
                GSM_LEVEL, (double) entity.getGsmLevel(),
                VOLTAGE, (double) entity.getOnboardVoltage(),
                CORNER_ACCELERATION, (double) entity.getEcoCornering(),
                ACCELERATION_UP, (double) entity.getEcoAcceleration(),
                ACCELERATION_DOWN, (double) entity.getEcoBraking(),
                GPS_ODOMETER, entity.getGpsOdometer(),
                IGNITION, (double) entity.getIgnition(),
                ENGINE_TIME, (double) entity.getEngineTime()
        );
    }
}
