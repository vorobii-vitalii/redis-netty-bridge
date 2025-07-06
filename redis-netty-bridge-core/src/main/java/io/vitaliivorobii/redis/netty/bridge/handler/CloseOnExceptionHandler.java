package io.vitaliivorobii.redis.netty.bridge.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class CloseOnExceptionHandler extends ChannelHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(CloseOnExceptionHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Unexpected error occurred. Closing TCP connection", cause);
        ctx.close();
    }
}
