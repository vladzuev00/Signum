package by.aurorasoft.signum.protocol.core;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationDeviceService {
    private final UnitService unitService;

    public Optional<Unit> authorize(String imei) {
        return this.unitService.findByTrackerImei(imei);
    }
}
