package io.vitaliivorobii.redis.netty.bridge.command.test;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.resp.types.RespSimpleString;

public class SelectRedisCommand implements RedisCommand {
	private final ChannelHandlerContext context;
	private final int dbIndex;

	public SelectRedisCommand(ChannelHandlerContext context, int dbIndex) {
		this.context = context;
		this.dbIndex = dbIndex;
	}

	@Override
	public void execute() {
		// save DB index to context
		context.writeAndFlush(new RespSimpleString("OK"));
	}
}
