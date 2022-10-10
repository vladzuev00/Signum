package by.aurorasoft.signum.service;

import by.aurorasoft.signum.dto.ChannelUnitDto;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class AuthorizationDeviceService {
    public Optional<ChannelUnitDto> authorize(LoginPackage loginPackage) {
        return Optional.empty();
    }
}
