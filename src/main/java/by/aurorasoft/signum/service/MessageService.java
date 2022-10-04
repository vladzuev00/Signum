package by.aurorasoft.signum.service;

import by.aurorasoft.signum.entity.MessageEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class MessageService {
    public int saveAndReturnSavedAmount(List<MessageEntity> messages) {
        return messages.size();
    }
}
