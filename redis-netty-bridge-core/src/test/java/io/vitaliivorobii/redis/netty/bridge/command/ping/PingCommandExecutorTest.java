package io.vitaliivorobii.redis.netty.bridge.command.ping;

import io.netty.channel.embedded.EmbeddedChannel;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.redis.netty.bridge.handler.InboundClientRedisRequestHandler;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespSimpleString;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PingCommandExecutorTest {

    @Test
    void executeGivenClientPassedNoArgs() {
        EmbeddedChannel channel = new EmbeddedChannel(new InboundClientRedisRequestHandler(
                new PingCommandExecutor()
        ));
        channel.writeInbound(new ClientRequest("PING", List.of()));

        assertThat(channel.<RespDataType>readOutbound()).isEqualTo(new RespSimpleString("PONG"));
    }

    @Test
    void executeGivenClientPassedMessage() {
        EmbeddedChannel channel = new EmbeddedChannel(new InboundClientRedisRequestHandler(
                new PingCommandExecutor()
        ));
        channel.writeInbound(new ClientRequest("PING", List.of("my message")));

        assertThat(channel.<RespDataType>readOutbound()).isEqualTo(new RespBulkString("my message"));
    }
}