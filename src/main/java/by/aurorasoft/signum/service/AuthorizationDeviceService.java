package by.aurorasoft.signum.service;

import by.aurorasoft.signum.dto.Unit;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class AuthorizationDeviceService {
    public Optional<Unit> authorize(LoginPackage loginPackage) {
        return Optional.empty();
    }
}
