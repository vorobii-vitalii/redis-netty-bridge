package io.vitaliivorobii.redis.netty.bridge.command.get;

import io.netty.channel.embedded.EmbeddedChannel;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.redis.netty.bridge.handler.InboundClientRedisRequestHandler;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetCommandExecutorTest {

    GetDataStrategy getDataStrategy = Mockito.mock(GetDataStrategy.class);

    @Test
    void givenErrorOccurredDuringFetchOfData() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new InboundClientRedisRequestHandler(
                new GetCommandExecutor(getDataStrategy)
        ));
        when(getDataStrategy.getData("X"))
                .thenReturn(Optional.of(CompletableFuture.failedFuture(new RuntimeException())));
        embeddedChannel.writeInbound(new ClientRequest("GET", List.of("X")));
        assertThat(embeddedChannel.<RespDataType>readOutbound()).isEqualTo(new RespNull());
    }

    @Test
    void givenDataSuccessfullyFetched() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new InboundClientRedisRequestHandler(
                new GetCommandExecutor(getDataStrategy)
        ));
        when(getDataStrategy.getData("X"))
                .thenReturn(Optional.of(CompletableFuture.completedFuture(new RespBulkString("DATA"))));
        embeddedChannel.writeInbound(new ClientRequest("GET", List.of("X")));
        assertThat(embeddedChannel.<RespDataType>readOutbound()).isEqualTo(new RespBulkString("DATA"));
    }

}