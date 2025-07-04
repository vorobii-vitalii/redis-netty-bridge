package io.vitaliivorobii.redis.netty.bridge.command.mget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.netty.channel.ChannelHandlerContext;
import io.vitalii.get.GetCommandStrategy;
import io.vitalii.reply.RespResponseWriter;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandFactory;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespDataType;

public class MGETCommandFactory implements RedisCommandFactory {
	private final GetCommandStrategy getCommandStrategy;

	public MGETCommandFactory(GetCommandStrategy getCommandStrategy) {
		this.getCommandStrategy = getCommandStrategy;
	}

	@Override
	public RedisCommand createRedisCommand(ClientRequest clientRequest, ChannelHandlerContext channelHandlerContext) {
		return () -> {
			ArrayRespResponseWriter respWriter = new ArrayRespResponseWriter();
			for (String argument : clientRequest.arguments()) {
				getCommandStrategy.createIfApplicable(argument, respWriter).ifPresent(RedisCommand::execute);
			}
			CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
			// TODO: fix design...
			CompletableFuture.allOf(future)
					.whenComplete((r, t) -> {
					});
			channelHandlerContext.writeAndFlush(respWriter.getArray());
		};
	}

	private static class ArrayRespResponseWriter implements RespResponseWriter {
		private final List<RespDataType> arr = new ArrayList<>();

		@Override
		public void reply(RespDataType respDataType) {
			arr.add(respDataType);
		}

		public RespArray getArray() {
			return new RespArray(arr);
		}
	}
}
