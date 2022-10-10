package by.aurorasoft.signum.protocol.wialon.handler.contextworker;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import static io.netty.util.AttributeKey.valueOf;

@Component
public final class ContextWorker {
    private static final String NAME_CHANNEL_ATTRIBUTE_KEY_TRACKER = "tracker";
    private static final AttributeKey<ChannelTracker> CHANNEL_ATTRIBUTE_KEY_TRACKER
            = valueOf(NAME_CHANNEL_ATTRIBUTE_KEY_TRACKER);

    public ChannelTracker findTracker(ChannelHandlerContext context) {
        final Channel channel = context.channel();
        final Attribute<ChannelTracker> trackerAttribute = channel.attr(CHANNEL_ATTRIBUTE_KEY_TRACKER);
        return trackerAttribute.get();
    }

    public void putTracker(ChannelHandlerContext context, ChannelTracker tracker) {
        final Channel channel = context.channel();
        final Attribute<ChannelTracker> trackerAttribute = channel.attr(CHANNEL_ATTRIBUTE_KEY_TRACKER);
        trackerAttribute.set(tracker);
    }
}
