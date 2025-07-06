package io.vitaliivorobii.redis.netty.bridge.command.get;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.CommandExecutionStrategy;
import io.vitaliivorobii.resp.types.RespNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GETCommandExecutionStrategy implements CommandExecutionStrategy<String> {
    private static final Logger log = LoggerFactory.getLogger(GETCommandExecutionStrategy.class);
    private final GetDataStrategy getDataStrategy;

    public GETCommandExecutionStrategy(GetDataStrategy getDataStrategy) {
        this.getDataStrategy = getDataStrategy;
    }

    @Override
    public boolean canHandle(ChannelHandlerContext channelHandlerContext, String args) {
        return true;
    }

    @Override
    public void execute(ChannelHandlerContext channelContext, String key) {
        getDataStrategy.getData(key)
                .ifPresent(v ->
                        v.whenComplete((resp, error) -> {
                            if (error != null) {
                                log.warn("Error occurred on GET command execution", error);
                                channelContext.writeAndFlush(new RespNull());
                            } else {
                                channelContext.writeAndFlush(resp);
                            }
                        }));
    }
}
