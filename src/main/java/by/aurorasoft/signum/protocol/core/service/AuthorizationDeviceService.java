package by.aurorasoft.signum.protocol.core.service;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.service.DeviceService;
import by.aurorasoft.signum.crud.service.message.MessageService;
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
    private final MessageService messageService;

    public Optional<Device> authorize(String imei, ChannelHandlerContext context) {
        this.contextManager.putDeviceImei(context, imei);
        final Optional<Device> optionalDevice = this.deviceService.findByImei(imei);
        optionalDevice.ifPresent(device -> {
            this.contextManager.putDevice(context, device);
            this.connectionManager.add(context);
            this.putLastMessageIfExist(context, device);
        });
        return optionalDevice;
    }

    private void putLastMessageIfExist(ChannelHandlerContext context, Device device) {
        this.messageService
                .findLastReceivedMessage(device.getId())
                .ifPresent(
                        message -> this.contextManager.putLastMessage(context, message));
    }
}
