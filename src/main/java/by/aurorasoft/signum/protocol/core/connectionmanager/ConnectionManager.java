package by.aurorasoft.signum.protocol.core.connectionmanager;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

//TODO: correct dependencies
@Component
public final class ConnectionManager {
    private final ContextManager contextManager;
    private final Map<Long, ChannelHandlerContext> deviceIdToContextMap;

    public ConnectionManager(ContextManager contextManager) {
        this.contextManager = contextManager;
        this.deviceIdToContextMap = new ConcurrentHashMap<>();
    }

    public void addContext(ChannelHandlerContext context) {
        final Unit associatedUnit = this.contextManager.findUnit(context);
        final Device device = associatedUnit.getDevice();
        this.deviceIdToContextMap.merge(device.getId(), context, (oldContext, newContext) -> {
            oldContext.close();
            return newContext;
        });
    }

    public Optional<ChannelHandlerContext> findContext(Device device) {
        return ofNullable(this.deviceIdToContextMap.get(device.getId()));
    }
}
