package io.vitaliivorobii.redis.netty.bridge.redis.insight.command;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespNull;
import jakarta.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class DownloadStringValueCommand implements RedisCommand {
    private static final Logger log = LoggerFactory.getLogger(DownloadStringValueCommand.class);
    private final URI redisInsightApiBaseUri;
    private final HttpClient httpClient;
    private final CompletionStage<String> databaseIdFuture;
    private final String key;
    private final Gson gson = new Gson();
    private final ChannelHandlerContext channelHandlerContext;

    public DownloadStringValueCommand(
            URI redisInsightApiBaseUri,
            HttpClient httpClient,
            CompletionStage<String> databaseIdFuture,
            String key,
            ChannelHandlerContext channelHandlerContext
    ) {
        this.redisInsightApiBaseUri = redisInsightApiBaseUri;
        this.httpClient = httpClient;
        this.databaseIdFuture = databaseIdFuture;
        this.key = key;
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public void execute() {
        System.out.println("key = " + key);
        databaseIdFuture
                .thenComposeAsync(dbInstance -> {
                    HttpRequest getValueRequest = HttpRequest.newBuilder()
                            .version(HttpClient.Version.HTTP_1_1)
                            .uri(UriBuilder.fromUri(redisInsightApiBaseUri)
                                    .path("/api/databases/{dbInstance}/string/download-value")
                                    .build(dbInstance))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(
                                    gson.toJson(Map.of("keyName", key))
                            ))
                            .build();
                    return httpClient.sendAsync(getValueRequest, HttpResponse.BodyHandlers.ofString());
                })
                .thenApply(HttpResponse::body)
                .whenComplete((value, error) -> {
                        if (error != null) {
                            log.error("Error occurred while downloading string value", error);
                            channelHandlerContext.writeAndFlush(new RespNull());
                        } else {
                            channelHandlerContext.writeAndFlush(new RespBulkString(value));
                        }
                });
    }

}
