package by.wialontransport.service;

import by.wialontransport.protocol.wialon.model.LoginPackage;
import org.springframework.stereotype.Service;

@Service
public final class AuthorizationDeviceService {
    public boolean authorize(LoginPackage loginPackage) {
        return true;
    }
}
