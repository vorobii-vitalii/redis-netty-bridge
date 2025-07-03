package io.vitaliivorobii.redis.netty.bridge.redis.insight.client;

import io.vitaliivorobii.redis.netty.bridge.redis.insight.client.dto.StringValueFetchRequest;

import java.util.concurrent.CompletionStage;

public interface StringValueFetcher {
    CompletionStage<String> fetchStringValue(StringValueFetchRequest request);
}
