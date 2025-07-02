package io.vitaliivorobii.redis.netty.bridge.redis.insight.command;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.redis.netty.bridge.command.args.LRangeArgs;
import io.vitaliivorobii.resp.types.*;
import jakarta.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class DownloadListElementsCommand implements RedisCommand {
    private static final Logger log = LoggerFactory.getLogger(DownloadStringValueCommand.class);
    private final URI redisInsightApiBaseUri;
    private final HttpClient httpClient;
    private final CompletionStage<String> databaseIdFuture;
    private final LRangeArgs lRangeArgs;
    private final Gson gson = new Gson();
    private final ChannelHandlerContext channelHandlerContext;

    public DownloadListElementsCommand(
            URI redisInsightApiBaseUri,
            HttpClient httpClient,
            CompletionStage<String> databaseIdFuture,
            LRangeArgs lRangeArgs,
            ChannelHandlerContext channelHandlerContext
    ) {
        this.redisInsightApiBaseUri = redisInsightApiBaseUri;
        this.httpClient = httpClient;
        this.databaseIdFuture = databaseIdFuture;
        this.lRangeArgs = lRangeArgs;
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public void execute() {
        // TODO: support for command with negative start...
        databaseIdFuture
                .thenComposeAsync(dbInstance -> {
                    HttpRequest getElementsRequest = HttpRequest.newBuilder()
                            .version(HttpClient.Version.HTTP_1_1)
                            .uri(UriBuilder.fromUri(redisInsightApiBaseUri)
                                    .path("/api/databases/{dbInstance}/list/get-elements")
                                    .build(dbInstance))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(
                                    gson.toJson(Map.of(
                                            "keyName", lRangeArgs.key(),
                                            "offset", lRangeArgs.start(),
                                            "count", lRangeArgs.stop() - lRangeArgs.start() + 1
                                    ))
                            ))
                            .build();
                    return httpClient.sendAsync(getElementsRequest, HttpResponse.BodyHandlers.ofString());
                })
                .whenComplete((response, error) -> {
                    if (error != null) {
                        log.error("Error occurred while downloading string value", error);
                        channelHandlerContext.writeAndFlush(
                                new RespSimpleError("error on download of value"));
                    } else {
                        if (response.statusCode() == HttpResponseStatus.OK.code()) {
                            JsonObject object = gson.fromJson(response.body(), JsonObject.class);
                            JsonArray array = object.getAsJsonArray("elements");
                            ArrayList<RespDataType> redisArray = new ArrayList<>(array.size());
                            for (int i = 0; i < array.size(); i++) {
                                redisArray.add(new RespBulkString(array.get(i).getAsString()));
                            }
                            channelHandlerContext.writeAndFlush(new RespArray(redisArray));
                        } else if (response.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
                            channelHandlerContext.writeAndFlush(new RespArray(List.of()));
                        } else {
                            log.error("Error occurred while downloading string value - {}", response.body());
                            channelHandlerContext.writeAndFlush(
                                    new RespSimpleError("error on fetch of string value"));
                        }
                    }
                });
    }

    //


}
