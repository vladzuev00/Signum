package by.aurorasoft.signum.dtomapper;

import by.aurorasoft.signum.dto.Message;
import by.aurorasoft.signum.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.entity.MessageEntity;
import by.aurorasoft.signum.entity.UnitEntity;
import by.nhorushko.crudgeneric.v2.mapper.ExtAbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public final class MessageMapper extends ExtAbstractMapper<MessageEntity, Message, Long, UnitEntity> {

    public MessageMapper(ModelMapper modelMapper, EntityManager entityManager) {
        super(modelMapper, MessageEntity.class, Message.class, entityManager, UnitEntity.class);
    }

    @Override
    protected void setRelation(UnitEntity unit, MessageEntity message) {
        message.setUnit(unit);
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
                entity.getParameters());
    }
}
