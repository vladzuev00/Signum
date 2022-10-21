package by.aurorasoft.signum.protocol.core.connectionmanager;

import by.aurorasoft.signum.crud.model.dto.Tracker;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Component
public final class ConnectionManager {
    private final ContextManager contextWorker;
    private final Map<Long, ChannelHandlerContext> trackerIdToContextMap;

    public ConnectionManager(ContextManager contextWorker) {
        this.contextWorker = contextWorker;
        this.trackerIdToContextMap = new ConcurrentHashMap<>();
    }

    public void addContext(ChannelHandlerContext context) {
        final Unit associatedUnit = this.contextWorker.findUnit(context);
        final Tracker tracker = associatedUnit.getTracker();
        this.trackerIdToContextMap.merge(tracker.getId(), context, (oldContext, newContext) -> {
            oldContext.close();
            return newContext;
        });
    }

    public Optional<ChannelHandlerContext> findContext(Tracker tracker) {
        return ofNullable(this.trackerIdToContextMap.get(tracker.getId()));
    }
}
