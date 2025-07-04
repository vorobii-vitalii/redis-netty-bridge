package io.vitaliivorobii.redis.netty.bridge.redis.insight.command;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.client.StringValueFetcher;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.client.dto.StringValueFetchRequest;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.dto.DatabaseKey;

import java.util.regex.Pattern;

public class RedisInsightProxyGetCommandExecutionStrategy extends RegexBasedCommandExecutionStrategy<String> {
    private final StringValueFetcher stringValueFetcher;
    private final DatabaseKey databaseKey;

    public RedisInsightProxyGetCommandExecutionStrategy(
            Pattern keyPattern,
            StringValueFetcher stringValueFetcher,
            DatabaseKey databaseKey
    ) {
        super(v -> v, keyPattern);
        this.stringValueFetcher = stringValueFetcher;
        this.databaseKey = databaseKey;
    }

    @Override
    public RedisCommand createCommand(ChannelHandlerContext context, String[] args, String fullKey) {
        return new DownloadStringValueCommand(
                stringValueFetcher,
                new StringValueFetchRequest(fullKey, databaseKey),
                context
        );
    }

}
