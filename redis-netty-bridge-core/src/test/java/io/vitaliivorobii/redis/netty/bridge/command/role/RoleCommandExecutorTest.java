package io.vitaliivorobii.redis.netty.bridge.command.role;

import io.netty.channel.embedded.EmbeddedChannel;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.redis.netty.bridge.handler.InboundClientRedisRequestHandler;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespBulkString;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleCommandExecutorTest {

    @Test
    void execute() {
        RoleCommandExecutor roleCommandExecutor = new RoleCommandExecutor();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new InboundClientRedisRequestHandler(roleCommandExecutor)
        );
        embeddedChannel.writeInbound(new ClientRequest("ROLE", List.of()));
        RespArray reply = embeddedChannel.readOutbound();
        assertThat(reply.getElements().stream().findFirst()).contains(new RespBulkString("master"));
    }

}