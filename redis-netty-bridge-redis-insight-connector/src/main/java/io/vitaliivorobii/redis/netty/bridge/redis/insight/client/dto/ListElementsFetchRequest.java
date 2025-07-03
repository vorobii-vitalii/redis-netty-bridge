package io.vitaliivorobii.redis.netty.bridge.redis.insight.client.dto;

import io.vitaliivorobii.redis.netty.bridge.redis.insight.dto.DatabaseKey;

public record ListElementsFetchRequest(String keyName, int offset, int count, DatabaseKey dbKey) {
}
