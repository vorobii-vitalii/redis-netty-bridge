package io.vitaliivorobii.redis.netty.bridge.command.get;

import io.vitaliivorobii.resp.types.RespDataType;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface GetDataStrategy {

    Optional<CompletableFuture<RespDataType>> getData(String key);

}
