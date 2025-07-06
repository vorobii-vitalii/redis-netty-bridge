package io.vitaliivorobii.redis.netty.bridge.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CloseOnExceptionHandlerTest {

    @Test
    void shouldCloseConnectionOnError() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new ChannelHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        throw new IllegalArgumentException("error occurred on parsing");
                    }
                },
                new CloseOnExceptionHandler()
        );
        channel.writeInbound(new Object());
        assertThat(channel.isActive()).isFalse();
    }
}
