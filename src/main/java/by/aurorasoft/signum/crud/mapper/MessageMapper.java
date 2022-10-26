package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterType;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.nhorushko.crudgeneric.v2.mapper.ExtAbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterType.*;

@Component
public final class MessageMapper extends ExtAbstractMapper<MessageEntity, Message, Long, DeviceEntity> {

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
    protected Message createDto(MessageEntity entity) {
        return new Message(
                entity.getId(),
                entity.getDateTime(),
                new GpsCoordinate(entity.getLatitude(), entity.getLongitude()),
                entity.getSpeed(),
                entity.getCourse(),
                entity.getAltitude(),
                entity.getAmountSatellite(),
                entity.getHdop(),
                mapParameters(entity));
    }

    private static void mapGpsCoordinate(Message source, MessageEntity destination) {
        final GpsCoordinate gpsCoordinate = source.getCoordinate();
        destination.setLatitude(gpsCoordinate.getLatitude());
        destination.setLongitude(gpsCoordinate.getLongitude());
    }

    private static void mapParameters(Message source, MessageEntity destination) {
        final Map<ParameterType, Float> parameterTypesToValues = source.getParameterTypesToValues();
        destination.setGsmLevelPercent(parameterTypesToValues.get(GSM_LEVEL).byteValue());
        destination.setVoltage(parameterTypesToValues.get(VOLTAGE));
        destination.setCornerAcceleration(parameterTypesToValues.get(CORNER_ACCELERATION));
        destination.setAccelerationUp(parameterTypesToValues.get(ACCELERATION_UP));
        destination.setAccelerationDown(parameterTypesToValues.get(ACCELERATION_DOWN));
    }

    private static Map<ParameterType, Float> mapParameters(MessageEntity entity) {
        return Map.of(
                GSM_LEVEL, (float) entity.getGsmLevelPercent(),
                VOLTAGE, entity.getVoltage(),
                CORNER_ACCELERATION, entity.getCornerAcceleration(),
                ACCELERATION_UP, entity.getAccelerationUp(),
                ACCELERATION_DOWN, entity.getAccelerationDown()
        );
    }
}
