package by.aurorasoft.signum.service;

import by.aurorasoft.signum.dto.Message;
import by.aurorasoft.signum.dtomapper.MessageMapper;
import by.aurorasoft.signum.entity.MessageEntity;
import by.aurorasoft.signum.entity.UnitEntity;
import by.aurorasoft.signum.repository.MessageRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractExtCRUDService;
import org.springframework.stereotype.Service;

@Service
public final class MessageService extends AbstractExtCRUDService<Long, MessageEntity, Message, Long, UnitEntity> {
    public MessageService(MessageMapper mapper, MessageRepository repository) {
        super(mapper, repository);
    }
}
