package by.aurorasoft.signum.protocol.wialon.service;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.service.UnitService;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationDeviceService {
    private final UnitService unitService;

    public Optional<Unit> authorize(LoginPackage loginPackage) {
        return this.unitService.findByTrackerImei(loginPackage.getImei());
    }
}
