package io.vitaliivorobii.redis.netty.bridge.redis.insight.client.http;

import com.google.gson.Gson;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.DatabaseIdProvider;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.client.StringValueFetcher;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.client.dto.StringValueFetchRequest;
import jakarta.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class StringValueFetcherImpl implements StringValueFetcher {
    private static final Logger log = LoggerFactory.getLogger(StringValueFetcherImpl.class);
    private final DatabaseIdProvider databaseIdProvider;
    private final HttpClient httpClient;
    private final URI baseURI;
    private final Gson gson = new Gson();

    public StringValueFetcherImpl(DatabaseIdProvider databaseIdProvider, HttpClient httpClient, URI baseURI) {
        this.databaseIdProvider = databaseIdProvider;
        this.httpClient = httpClient;
        this.baseURI = baseURI;
    }

    @Override
    public CompletionStage<String> fetchStringValue(StringValueFetchRequest request) {
        return databaseIdProvider.getDatabaseId(request.dbKey())
                .thenComposeAsync(dbInstance -> {
                    HttpRequest getValueRequest = HttpRequest.newBuilder()
                            .version(HttpClient.Version.HTTP_1_1)
                            .uri(UriBuilder.fromUri(baseURI)
                                    .path("/api/databases/{dbInstance}/string/download-value")
                                    .build(dbInstance))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(
                                    gson.toJson(Map.of("keyName", request.key()))
                            ))
                            .build();
                    return httpClient.sendAsync(getValueRequest, HttpResponse.BodyHandlers.ofString());
                })
                .thenCompose(response -> {
                    if (response.statusCode() == HttpResponseStatus.OK.code()) {
                        return CompletableFuture.completedFuture(response.body());
                    } else if (response.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
                        return CompletableFuture.completedFuture(null);
                    } else {
                        log.error("Error occurred while downloading string value - {}", response.body());
                        return CompletableFuture.failedFuture(new RuntimeException("error on fetch of string value"));
                    }
                });
    }
}
