package io.vitaliivorobii.redis.netty.bridge.command.get;

import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FirstMatchGetDataStrategy implements GetDataStrategy {
    private final List<GetDataStrategy> getCommandStrategies;

    public FirstMatchGetDataStrategy(List<GetDataStrategy> getCommandStrategies) {
        this.getCommandStrategies = getCommandStrategies;
    }

    @Override
    public Optional<CompletableFuture<RespDataType>> getData(String key) {
        Optional<CompletableFuture<RespDataType>> firstMatch = getCommandStrategies.stream()
                .flatMap(v -> v.getData(key).stream())
                .findFirst();
        if (firstMatch.isPresent()) {
            return firstMatch;
        }
        return Optional.of(CompletableFuture.completedFuture(new RespNull()));
    }

}
