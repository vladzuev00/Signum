package by.aurorasoft.signum.kafka.consumer;

import by.aurorasoft.kafka.consumer.KafkaConsumerGenericRecordBatch;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.aurorasoft.signum.crud.service.MessageService;
import by.aurorasoft.signum.kafka.producer.KafkaSavedMessageProducer;
import by.aurorasoft.signum.kafka.producer.transportable.TransportableMessage.FieldName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static by.aurorasoft.signum.kafka.producer.transportable.TransportableMessage.FieldName.*;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public final class KafkaInboundMessageConsumer extends KafkaConsumerGenericRecordBatch<Long, Message> {
    private static final String LOG_TEMPLATE_RECEIVE_INBOUND_MESSAGES_FROM_KAFKA
            = "Inbound messages were received from kafka. Messages: %s.";
    private static final String LOG_TEMPLATE_SAVE_INBOUND_MESSAGES = "Inbound messages were saved. Messages: %s";

    private final MessageService messageService;
    private final KafkaSavedMessageProducer savedMessageProducer;

    @Override
    @KafkaListener(
            topics = "${kafka.topic.inbound-messages.name}",
            groupId = "${kafka.topic.inbound-messages.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactoryInboundMessages")
    public void listen(List<ConsumerRecord<Long, GenericRecord>> records) {
        final List<Message> messages = this.map(records);

        log.info(format(LOG_TEMPLATE_RECEIVE_INBOUND_MESSAGES_FROM_KAFKA, messages));
        final List<Message> savedMessages = this.messageService.saveAll(messages);
        log.info(format(LOG_TEMPLATE_SAVE_INBOUND_MESSAGES, messages));

        this.savedMessageProducer.send(savedMessages);
    }


    @Override
    protected Message map(GenericRecord genericRecord) {
        return Message.builder()
                .id(super.getObject(genericRecord, ID.getName()))
                .datetime(super.getInstant(genericRecord, DATE_TIME_EPOCH_SECOND.getName()))
                .coordinate(this.mapCoordinate(genericRecord))
                .speed(super.getInt(genericRecord, SPEED.getName()))
                .amountSatellite(super.getInt(genericRecord, AMOUNT_SATELLITE.getName()))
                .course(super.getInt(genericRecord, COURSE.getName()))
                .altitude(super.getInt(genericRecord, ALTITUDE.getName()))
                .parameterNamesByValues(this.mapParameters(genericRecord))
                .type(this.mapType(genericRecord))
                .gpsOdometer(super.getDouble(genericRecord, GPS_ODOMETER.getName()))
                .ignition(super.getInt(genericRecord, IGNITION.getName()))
                .engineTime(super.getLong(genericRecord, ENGINE_TIME.getName()))
                .shock(super.getDouble(genericRecord, SHOCK.getName()))
                .device(this.mapDevice(genericRecord))
                .build();
    }

    private List<Message> map(List<ConsumerRecord<Long, GenericRecord>> records) {
        return records.stream()
                .map(this::map)
                .collect(toList());
    }

    private GpsCoordinate mapCoordinate(GenericRecord genericRecord) {
        return new GpsCoordinate(
                super.getFloat(genericRecord, LATITUDE.getName()),
                super.getFloat(genericRecord, LONGITUDE.getName()));
    }

    private Map<ParameterName, Double> mapParameters(GenericRecord genericRecord) {
        return Map.of(
                ParameterName.GSM_LEVEL, super.getDouble(genericRecord, FieldName.GSM_LEVEL.getName()),
                ParameterName.VOLTAGE, super.getDouble(genericRecord, FieldName.VOLTAGE.getName()),
                ParameterName.CORNER_ACCELERATION, super.getDouble(genericRecord, FieldName.CORNER_ACCELERATION.getName()),
                ParameterName.ACCELERATION_UP, super.getDouble(genericRecord, FieldName.ACCELERATION_UP.getName()),
                ParameterName.ACCELERATION_DOWN, super.getDouble(genericRecord, FieldName.ACCELERATION_DOWN.getName())
        );
    }

    private MessageType mapType(GenericRecord genericRecord) {
        return MessageType.valueOf(super.getString(genericRecord, TYPE.getName()));
    }

    private Device mapDevice(GenericRecord genericRecord) {
        return Device.builder()
                .id(super.getLong(genericRecord, DEVICE_ID.getName()))
                .build();
    }
}
