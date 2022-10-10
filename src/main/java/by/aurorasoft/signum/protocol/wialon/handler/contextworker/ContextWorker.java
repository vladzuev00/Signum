package by.aurorasoft.signum.protocol.wialon.handler.contextworker;

import by.aurorasoft.signum.dto.ChannelUnitDto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import static io.netty.util.AttributeKey.valueOf;

@Component
public final class ContextWorker {
    private static final String NAME_CHANNEL_ATTRIBUTE_KEY_UNIT = "unit";
    private static final AttributeKey<ChannelUnitDto> CHANNEL_ATTRIBUTE_KEY_UNIT
            = valueOf(NAME_CHANNEL_ATTRIBUTE_KEY_UNIT);

    public ChannelUnitDto findUnit(ChannelHandlerContext context) {
        final Channel channel = context.channel();
        final Attribute<ChannelUnitDto> unitAttribute = channel.attr(CHANNEL_ATTRIBUTE_KEY_UNIT);
        return unitAttribute.get();
    }

    public void putUnit(ChannelHandlerContext context, ChannelUnitDto unit) {
        final Channel channel = context.channel();
        final Attribute<ChannelUnitDto> unitAttribute = channel.attr(CHANNEL_ATTRIBUTE_KEY_UNIT);
        unitAttribute.set(unit);
    }
}
