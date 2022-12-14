package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;

@Component
public final class MessageMapper extends AbsMapperEntityDto<MessageEntity, Message> {
    private static final Long NOT_DEFINED_ID = 0L;

    private final EntityManager entityManager;

    public MessageMapper(ModelMapper modelMapper, EntityManager entityManager) {
        super(modelMapper, MessageEntity.class, Message.class);
        this.entityManager = entityManager;
    }

    @Override
    protected void mapSpecificFields(Message source, MessageEntity destination) {
        mapGpsCoordinate(source, destination);
        mapParameters(source, destination);
        this.mapDevice(source, destination);
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
                mapParameters(entity),
                entity.getType(),
                entity.getGpsOdometer(),
                entity.getIgnition(),
                entity.getEngineTime(),
                entity.getShock(),
                mapDeviceId(entity));
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
        destination.setEcoAcceleration(source.getParameter(ACCELERATION_UP).floatValue());
        destination.setEcoBraking(source.getParameter(ACCELERATION_DOWN).floatValue());
    }

    private void mapDevice(Message source, MessageEntity destination) {
        destination.setDevice(this.entityManager.getReference(DeviceEntity.class, source.getDeviceId()));
    }

    private static Map<ParameterName, Double> mapParameters(MessageEntity entity) {
        return Map.of(
                GSM_LEVEL, (double) entity.getGsmLevel(),
                VOLTAGE, (double) entity.getOnboardVoltage(),
                CORNER_ACCELERATION, (double) entity.getEcoCornering(),
                ACCELERATION_UP, (double) entity.getEcoAcceleration(),
                ACCELERATION_DOWN, (double) entity.getEcoBraking()
        );
    }

    private static Long mapDeviceId(MessageEntity entity) {
        return entity.getDevice() != null ? entity.getDevice().getId() : NOT_DEFINED_ID;
    }
}
