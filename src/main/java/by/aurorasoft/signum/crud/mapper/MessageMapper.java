package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
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
