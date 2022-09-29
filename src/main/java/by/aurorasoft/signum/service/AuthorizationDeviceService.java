package by.aurorasoft.signum.service;

import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import org.springframework.stereotype.Service;

@Service
public final class AuthorizationDeviceService {
    public boolean authorize(LoginPackage loginPackage) {
        return true;
    }
}
