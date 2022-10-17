package by.aurorasoft.signum.protocol.wialon.connectionmanager;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.wialon.handler.contextworker.ContextWorker;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public final class ConnectionManager {
    private final ContextWorker contextWorker;
    private final Map<String, ChannelHandlerContext> trackerImeiToChannelHandlerContextMap;

    public ConnectionManager(ContextWorker contextWorker) {
        this.contextWorker = contextWorker;
        this.trackerImeiToChannelHandlerContextMap = new ConcurrentHashMap<>();
    }

    public void addContext(ChannelHandlerContext channelHandlerContext) {
        final Unit associatedUnit = this.contextWorker.findUnit(channelHandlerContext);
        final String trackerImei = associatedUnit.getTracker().getImei();
        this.trackerImeiToChannelHandlerContextMap.put(trackerImei, channelHandlerContext);
    }

    public ChannelHandlerContext findContext(String imei) {
        return this.trackerImeiToChannelHandlerContextMap.get(imei);
    }
}
