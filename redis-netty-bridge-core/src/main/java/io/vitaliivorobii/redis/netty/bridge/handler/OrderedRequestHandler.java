package io.vitaliivorobii.redis.netty.bridge.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayDeque;

public class OrderedRequestHandler extends ChannelHandlerAdapter {
    private final ArrayDeque<Object> messageQueue = new ArrayDeque<>();
    private boolean isProcessing;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().config().setAutoRead(false);
        ctx.read();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        messageQueue.addLast(msg);
        if (!isProcessing) {
            isProcessing = true;
            ctx.fireChannelRead(messageQueue.pollFirst());
        }
    }

    @Override
    public void flush(ChannelHandlerContext ctx) {
        ctx.flush();
        if (messageQueue.isEmpty()) {
            isProcessing = false;
            ctx.read();
        } else {
            ctx.fireChannelRead(messageQueue.pollFirst());
        }
    }
}
