package by.aurorasoft.signum.protocol.wialon.connectionmanager;

import by.aurorasoft.signum.crud.model.dto.Tracker;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.wialon.contextmanager.ContextManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public final class ConnectionManager {
    private final ContextManager contextWorker;
    private final Map<Long, ChannelHandlerContext> trackerIdToChannelHandlerContextMap;

    public ConnectionManager(ContextManager contextWorker) {
        this.contextWorker = contextWorker;
        this.trackerIdToChannelHandlerContextMap = new ConcurrentHashMap<>();
    }

    public void addContext(ChannelHandlerContext context) {
        final Unit associatedUnit = this.contextWorker.findUnit(context);
        final Tracker tracker = associatedUnit.getTracker();
        this.trackerIdToChannelHandlerContextMap.put(tracker.getId(), context);
    }

    public ChannelHandlerContext findContext(Tracker tracker) {
        return this.trackerIdToChannelHandlerContextMap.get(tracker.getId());
    }
}
