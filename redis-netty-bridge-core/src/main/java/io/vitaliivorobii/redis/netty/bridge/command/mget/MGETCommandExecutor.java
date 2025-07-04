package io.vitaliivorobii.redis.netty.bridge.command.mget;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.CommandExecutionStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.get.GetDataStrategy;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MGETCommandExecutor implements CommandExecutionStrategy<List<String>> {
    private final GetDataStrategy getCommandStrategy;

    public MGETCommandExecutor(GetDataStrategy getCommandStrategy) {
        this.getCommandStrategy = getCommandStrategy;
    }

    @Override
    public boolean canHandle(ChannelHandlerContext channelHandlerContext, List<String> args) {
        channelHandlerContext.at
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
                    for (int i = 0; i < n; i++) {
                        CompletableFuture<RespDataType> future = array[i];
                        results.add(future.getNow(new RespNull()));
                    }
                    RespArray respArray = new RespArray(results);
                    channelContext.writeAndFlush(respArray);
                });
    }
}
