package io.vitaliivorobii.redis.netty.bridge.command.sentinel;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public interface LocalInetSocketAddressExtractor {
    InetSocketAddress extractLocalSocketAddress(ChannelHandlerContext channelContext);
}
