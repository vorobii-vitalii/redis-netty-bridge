package io.vitaliivorobii.redis.netty.bridge.command.test;

import java.util.List;
import java.util.Optional;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.CommandExecutionStrategy;

public class GetMasterAddressSentinelCommandExecutionStrategy implements CommandExecutionStrategy<List<String>> {

	public static final String GET_MASTER_ADDR_BY_NAME = "GET-MASTER-ADDR-BY-NAME";

	@Override
	public Optional<RedisCommand> createIfApplicable(ChannelHandlerContext channelHandlerContext, List<String> stringList) {
		if (stringList.isEmpty() || !stringList.getFirst().equalsIgnoreCase(GET_MASTER_ADDR_BY_NAME)) {
			return Optional.empty();
		}
		return Optional.of(new ReturnLocalAddressCommand(channelHandlerContext));
	}

}
