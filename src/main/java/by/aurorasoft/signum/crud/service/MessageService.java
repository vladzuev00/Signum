package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.mapper.MessageMapper;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.repository.MessageRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import by.nhorushko.crudgeneric.v2.service.AbsServiceExtCRUD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MessageService extends AbsServiceCRUD<Long, MessageEntity, Message, MessageRepository> {
    public MessageService(MessageMapper mapper, MessageRepository repository) {
        super(mapper, repository);
    }

    public Optional<Message> findLastMessage(Long deviceId) {
        final Optional<MessageEntity> optionalEntity = super.repository.findLastMessage(deviceId);
        return optionalEntity.map(super.mapper::toDto);
    }
}
