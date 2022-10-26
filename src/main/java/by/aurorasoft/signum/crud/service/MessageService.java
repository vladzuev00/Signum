package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.mapper.MessageMapper;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.aurorasoft.signum.crud.repository.MessageRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractExtCRUDService;
import org.springframework.stereotype.Service;

@Service
public final class MessageService extends AbstractExtCRUDService<Long, MessageEntity, Message, Long, DeviceEntity> {
    public MessageService(MessageMapper mapper, MessageRepository repository) {
        super(mapper, repository);
    }
}
