package by.aurorasoft.signum.protocol.core.service;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.service.DeviceService;
import by.aurorasoft.signum.protocol.core.connectionmanager.ConnectionManager;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationDeviceService {
    private final ContextManager contextManager;
    private final DeviceService deviceService;
    private final ConnectionManager connectionManager;

    public Optional<Device> authorize(ChannelHandlerContext context, String imei) {
        this.contextManager.putDeviceImei(context, imei);
        final Optional<Device> optionalDevice = this.deviceService.findByImei(imei);
        optionalDevice.ifPresent(device -> {
            this.contextManager.putDevice(context, device);
            this.connectionManager.addContext(context);
        });
        return optionalDevice;
    }
}
