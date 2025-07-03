package io.vitaliivorobii.redis.netty.bridge.redis.insight.client.dto;

import io.vitaliivorobii.redis.netty.bridge.redis.insight.dto.DatabaseKey;

public record StringValueFetchRequest(String key, DatabaseKey dbKey) {
}
