package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.mapper.DeviceMapper;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.repository.DeviceRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class DeviceService extends AbstractCrudService<Long, DeviceEntity, Device> {
    public DeviceService(DeviceMapper mapper, DeviceRepository repository) {
        super(mapper, repository);
    }

    public Optional<Device> findByImei(String imei) {
        final DeviceRepository deviceRepository = (DeviceRepository) super.repository;
        final Optional<DeviceEntity> optionalEntity = deviceRepository.findByImei(imei);
        return optionalEntity.map(super.mapper::toDto);
    }
}
