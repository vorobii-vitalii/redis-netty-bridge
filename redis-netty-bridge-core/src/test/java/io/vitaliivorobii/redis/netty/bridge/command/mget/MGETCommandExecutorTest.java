package io.vitaliivorobii.redis.netty.bridge.command.mget;

import io.netty.channel.embedded.EmbeddedChannel;
import io.vitaliivorobii.redis.netty.bridge.command.get.GetDataStrategy;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.redis.netty.bridge.handler.InboundClientRedisRequestHandler;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespNull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MGETCommandExecutorTest {

    GetDataStrategy getCommandStrategy = mock(GetDataStrategy.class);

    @Test
    void execute() {

        EmbeddedChannel channel = new EmbeddedChannel(new InboundClientRedisRequestHandler(
                new MGETCommandExecutor(getCommandStrategy)
        ));

        ClientRequest clientRequest = new ClientRequest("MGET", List.of("X", "Y", "Z"));

        when(getCommandStrategy.getData("X"))
                .thenReturn(Optional.of(CompletableFuture.completedFuture(new RespBulkString("X R"))));
        when(getCommandStrategy.getData("Y"))
                .thenReturn(Optional.of(CompletableFuture.failedFuture(new RuntimeException("error"))));
        when(getCommandStrategy.getData("Z"))
                .thenReturn(Optional.of(CompletableFuture.completedFuture(new RespBulkString("Z R"))));

        channel.writeInbound(clientRequest);

        assertThat(channel.<RespDataType>readOutbound()).isEqualTo(new RespArray(List.of(
                new RespBulkString("X R"),
                new RespNull(),
                new RespBulkString("Z R")
        )));

    }

}