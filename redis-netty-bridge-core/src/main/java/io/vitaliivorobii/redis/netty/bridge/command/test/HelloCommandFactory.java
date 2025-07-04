package io.vitaliivorobii.redis.netty.bridge.command.test;

import io.netty.channel.ChannelHandlerContext;
import io.vitalii.command.HelloCommand;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandFactory;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

public class HelloCommandFactory implements RedisCommandFactory {
	@Override
	public RedisCommand createRedisCommand(ClientRequest clientRequest, ChannelHandlerContext channelHandlerContext) {
		return new HelloCommand(channelHandlerContext);
	}
}
