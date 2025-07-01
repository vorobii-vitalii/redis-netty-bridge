package io.vitaliivorobii.redis.netty.bridge.domain;

import java.util.List;

public record ClientRequest(String commandName, List<String> arguments) {
}
