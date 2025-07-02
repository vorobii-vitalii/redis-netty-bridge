package io.vitaliivorobii.redis.netty.bridge.command.args;

public record LRangeArgs(String key, int start, int stop) {
}
