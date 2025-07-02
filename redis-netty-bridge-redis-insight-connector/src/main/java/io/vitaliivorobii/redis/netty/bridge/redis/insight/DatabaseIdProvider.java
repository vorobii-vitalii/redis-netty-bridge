package io.vitaliivorobii.redis.netty.bridge.redis.insight;

import io.vitaliivorobii.redis.netty.bridge.redis.insight.dto.DatabaseKey;

import java.util.concurrent.CompletionStage;

public interface DatabaseIdProvider {
    CompletionStage<String> getDatabaseId(DatabaseKey databaseKey);
}
