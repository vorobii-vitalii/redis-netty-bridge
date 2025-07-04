package io.vitaliivorobii.redis.netty.bridge.command.test;

import io.vitaliivorobii.resp.types.RespDataType;

public interface RespResponseWriter {
	void reply(RespDataType respDataType);
}
