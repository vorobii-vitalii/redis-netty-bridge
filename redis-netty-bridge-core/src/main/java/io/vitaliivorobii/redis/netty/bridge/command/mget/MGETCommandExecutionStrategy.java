package io.vitaliivorobii.redis.netty.bridge.command.mget;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.CommandExecutionStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.get.GetDataStrategy;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MGETCommandExecutionStrategy implements CommandExecutionStrategy<List<String>> {
    private static final Logger log = LoggerFactory.getLogger(MGETCommandExecutionStrategy.class);
    private final GetDataStrategy getCommandStrategy;

    public MGETCommandExecutionStrategy(GetDataStrategy getCommandStrategy) {
        this.getCommandStrategy = getCommandStrategy;
    }

    @Override
    public boolean canHandle(ChannelHandlerContext channelHandlerContext, List<String> args) {
        return true;
    }

    @Override
    public void execute(ChannelHandlerContext channelContext, List<String> keys) {
        CompletableFuture<RespDataType>[] array = keys.stream()
                .map(getCommandStrategy::getData)
                .flatMap(Optional::stream)
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(array)
                .whenComplete((ignored, ignoredError) -> {
                    // Best effort strategy
                    int n = array.length;
                    List<RespDataType> results = new ArrayList<>(n);
                    int idx = 0;
                    for (CompletableFuture<RespDataType> future : array) {
                        if (future.isCompletedExceptionally()) {
                            log.warn("Failed to get data for key {}", keys.get(idx));
                            results.add(new RespNull());
                        } else {
                            results.add(future.getNow(new RespNull()));
                        }
                        idx++;
                    }
                    RespArray respArray = new RespArray(results);
                    channelContext.writeAndFlush(respArray);
                });
    }
}
