package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.mapper.MessageMapper;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.repository.MessageRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractExtCRUDService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageService extends AbstractExtCRUDService<Long, MessageEntity, Message, Long, DeviceEntity> {
    public MessageService(MessageMapper mapper, MessageRepository repository) {
        super(mapper, repository);
    }
}
