package io.vitaliivorobii.redis.netty.bridge.command.test;

import java.net.InetSocketAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespBulkString;

public class ReturnLocalAddressCommand implements RedisCommand {
	private static final Logger log = LoggerFactory.getLogger(ReturnLocalAddressCommand.class);
	private final ChannelHandlerContext context;

	public ReturnLocalAddressCommand(ChannelHandlerContext context) {
		this.context = context;
	}

	@Override
	public void execute() {
		InetSocketAddress socketAddress = (InetSocketAddress) context.channel().localAddress();
		log.info("My local address is {}", socketAddress);
		context.writeAndFlush(new RespArray(List.of(
				new RespBulkString(socketAddress.getHostString()),
				new RespBulkString(String.valueOf(socketAddress.getPort()))
		)));
	}
}
