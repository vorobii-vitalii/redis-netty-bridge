package io.vitaliivorobii.redis.netty.bridge.command;

import io.vitaliivorobii.resp.types.RespDataType;

import java.util.concurrent.CompletionStage;

public interface RespResponseWriter {
    CompletionStage<?> reply(RespDataType respDataType);
}
