package by.aurorasoft.signum.kafka.consumer.message;

import by.aurorasoft.kafka.consumer.KafkaConsumerGenericRecordBatch;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.aurorasoft.signum.kafka.producer.transportable.TransportableMessage.FieldName;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Map;

import static by.aurorasoft.signum.kafka.producer.transportable.TransportableMessage.FieldName.*;
import static java.util.stream.Collectors.toList;

public abstract class AbstractKafkaMessageConsumer extends KafkaConsumerGenericRecordBatch<Long, Message> {

    @Override
    protected final Message map(GenericRecord genericRecord) {
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
                .deviceId(super.getLong(genericRecord, DEVICE_ID.getName()))
                .build();
    }

    protected final List<Message> map(List<ConsumerRecord<Long, GenericRecord>> records) {
        return records.stream()
                .map(this::map)
                .collect(toList());
    }

    private Message.GpsCoordinate mapCoordinate(GenericRecord genericRecord) {
        return new Message.GpsCoordinate(
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
}
