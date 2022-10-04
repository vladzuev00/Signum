package by.aurorasoft.signum.service;

import by.aurorasoft.signum.dto.Message;
import by.aurorasoft.signum.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class MessageService {
    private final MessageRepository messageRepository;

    public int saveAndReturnSavedAmount(List<Message> messages) {
        //this.messageRepository.saveAll(messages);
        return messages.size();
    }
}
