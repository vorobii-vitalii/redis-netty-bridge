package io.vitaliivorobii.redis.netty.bridge.redis.insight.command;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.client.StringValueFetcher;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.client.dto.StringValueFetchRequest;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespNull;
import io.vitaliivorobii.resp.types.RespSimpleError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadStringValueCommand implements RedisCommand {
    private static final Logger log = LoggerFactory.getLogger(DownloadStringValueCommand.class);

    private final StringValueFetchRequest stringValueFetchRequest;
    private final StringValueFetcher stringValueFetcher;
    private final ChannelHandlerContext channelHandlerContext;

    public DownloadStringValueCommand(
            StringValueFetcher stringValueFetcher,
            StringValueFetchRequest stringValueFetchRequest,
            ChannelHandlerContext channelHandlerContext
    ) {
        this.stringValueFetcher = stringValueFetcher;
        this.stringValueFetchRequest = stringValueFetchRequest;
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public void execute() {
        stringValueFetcher.fetchStringValue(stringValueFetchRequest)
                .whenComplete((resp, error) -> {
                    if (error != null) {
                        log.error("Error occurred while downloading string value", error);
                        channelHandlerContext.writeAndFlush(
                                new RespSimpleError("error on download of value"));
                    } else {
                        RespDataType redisReply = resp == null ? new RespNull() : new RespBulkString(resp);
                        channelHandlerContext.writeAndFlush(redisReply);
                    }
                });
    }

}
