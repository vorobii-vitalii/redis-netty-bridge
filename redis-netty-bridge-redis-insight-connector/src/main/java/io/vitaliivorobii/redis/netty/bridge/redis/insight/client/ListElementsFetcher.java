package io.vitaliivorobii.redis.netty.bridge.redis.insight.client;

import io.vitaliivorobii.redis.netty.bridge.redis.insight.client.dto.ListElementsFetchRequest;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ListElementsFetcher {

    CompletionStage<List<String>> fetchListElements(ListElementsFetchRequest request);

}
