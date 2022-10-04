package by.aurorasoft.signum.service;

import by.aurorasoft.signum.dto.Message;
import by.aurorasoft.signum.entity.MessageEntity;
import by.aurorasoft.signum.entity.TrackerEntity;
import by.aurorasoft.signum.repository.MessageRepository;
import by.aurorasoft.signum.repository.TrackerRepository;
import by.aurorasoft.signum.service.exception.NoSuchTrackerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public final class MessageService {
    private final MessageRepository messageRepository;
    private final TrackerRepository trackerRepository;

    public int saveAndReturnSavedAmount(List<Message> messages, String trackerImei) {
        final Optional<TrackerEntity> optionalTracker = this.trackerRepository.findByImei(trackerImei);
        final TrackerEntity tracker = optionalTracker.orElseThrow(NoSuchTrackerException::new);
        final List<MessageEntity> messageEntities = messages.stream()
                .map(message -> mapToEntity(message, tracker))
                .collect(toList());
        this.messageRepository.saveAll(messageEntities);
        return messages.size();
    }

    private static MessageEntity mapToEntity(Message message, TrackerEntity tracker) {
        return MessageEntity.builder()
                .tracker(tracker)
                .dateTime(message.getDateTime())
                .latitude(message.getCoordinate().getLatitude())
                .longitude(message.getCoordinate().getLongitude())
                .speed(message.getSpeed())
                .course(message.getCourse())
                .altitude(message.getAltitude())
                .amountSatellite(message.getAmountSatellite())
                .hdop(message.getHdop())
                .parameters(message.getParameters())
                .build();
    }
}
