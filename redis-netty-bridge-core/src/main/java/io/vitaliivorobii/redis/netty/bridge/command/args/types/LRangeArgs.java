package io.vitaliivorobii.redis.netty.bridge.command.args.types;

public record LRangeArgs(String key, int start, int stop) {
}
