package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.mapper.DeviceMapper;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.repository.DeviceRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DeviceService extends AbsServiceCRUD<Long, DeviceEntity, Device, DeviceRepository> {
    public DeviceService(DeviceMapper mapper, DeviceRepository repository) {
        super(mapper, repository);
    }

    public Optional<Device> findByImei(String imei) {
        final Optional<DeviceEntity> optionalEntity = super.repository.findByImei(imei);
        return optionalEntity.map(super.mapper::toDto);
    }
}
