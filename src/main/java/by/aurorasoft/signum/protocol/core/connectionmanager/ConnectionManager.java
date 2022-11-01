package by.aurorasoft.signum.protocol.core.connectionmanager;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

//TODO: correct circular dependencies
@Component
public final class ConnectionManager {
    private final ContextManager contextManager;
    private final Map<Long, ChannelHandlerContext> deviceIdToContextMap;

    public ConnectionManager(ContextManager contextManager) {
        this.contextManager = contextManager;
        this.deviceIdToContextMap = new ConcurrentHashMap<>();
    }

    public void add(ChannelHandlerContext context) {
        final Device associatedDevice = this.contextManager.findDevice(context);
        this.deviceIdToContextMap.merge(associatedDevice.getId(), context, (oldContext, newContext) -> {
            oldContext.close();
            return newContext;
        });
    }

    public Optional<ChannelHandlerContext> find(Long deviceId) {
        return ofNullable(this.deviceIdToContextMap.get(deviceId));
    }

    public void remove(Long deviceId) {
        this.deviceIdToContextMap.remove(deviceId);
    }
}
