package io.vitaliivorobii.redis.netty.bridge.command.select;

import io.netty.channel.embedded.EmbeddedChannel;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.redis.netty.bridge.handler.InboundClientRedisRequestHandler;
import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespSimpleString;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SelectCommandExecutorTest {

    @Test
    void shouldSetDBIndexInContext() {
        EmbeddedChannel channel = new EmbeddedChannel(new InboundClientRedisRequestHandler(
                new SelectCommandExecutor()
        ));
        channel.writeInbound(new ClientRequest("SELECT", List.of("24")));

        assertThat(channel.<RespDataType>readOutbound()).isEqualTo(new RespSimpleString("OK"));
        assertThat(channel.attr(SelectedDatabase.INSTANCE).get()).isEqualTo(24);
    }
}