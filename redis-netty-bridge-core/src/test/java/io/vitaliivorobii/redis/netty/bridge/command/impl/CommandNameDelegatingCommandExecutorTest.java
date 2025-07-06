package io.vitaliivorobii.redis.netty.bridge.command.impl;

import io.netty.channel.embedded.EmbeddedChannel;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.redis.netty.bridge.handler.InboundClientRedisRequestHandler;
import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespSimpleError;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CommandNameDelegatingCommandExecutorTest {

    RedisCommandExecutor getCommandExecutor = mock(RedisCommandExecutor.class);

    RedisCommandExecutor setCommandExecutor = mock(RedisCommandExecutor.class);

    InboundClientRedisRequestHandler inboundClientRedisRequestHandler =
            new InboundClientRedisRequestHandler(
                    new CommandNameDelegatingCommandExecutor(
                            Map.of(
                                    "GET", getCommandExecutor,
                                    "SET", setCommandExecutor
                            )));


    @Test
    void givenCommandNotRecognized() {
        EmbeddedChannel channel = new EmbeddedChannel(inboundClientRedisRequestHandler);
        channel.writeInbound(new ClientRequest("HGET", List.of()));
        assertThat(channel.<RespDataType>readOutbound())
                .isEqualTo(new RespSimpleError("Command unrecognized"));
    }

    @Test
    void givenCommandRecognizedVerifyCommandDispatchedToCorrectHandler() {
        EmbeddedChannel channel = new EmbeddedChannel(inboundClientRedisRequestHandler);
        ClientRequest getRequest = new ClientRequest("GET", List.of("KEY"));
        ClientRequest setRequest = new ClientRequest("SET", List.of("KEY", "VALUE"));
        channel.writeInbound(getRequest);
        channel.writeInbound(setRequest);
        verify(getCommandExecutor).executeRedisCommand(eq(getRequest), any());
        verify(setCommandExecutor).executeRedisCommand(eq(setRequest), any());
        verifyNoMoreInteractions(getCommandExecutor, setCommandExecutor);

    }
}