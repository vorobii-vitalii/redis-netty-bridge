package io.vitaliivorobii.redis.netty.bridge.command.sentinel;

import io.netty.channel.embedded.EmbeddedChannel;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.redis.netty.bridge.handler.InboundClientRedisRequestHandler;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespDataType;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SentinelCommandExecutorTest {

    LocalInetSocketAddressExtractor localInetSocketAddressExtractor = mock(LocalInetSocketAddressExtractor.class);

    @Test
    void executeGetMasterAddressCommandExpectLocalAddressIsReturned() {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("172.31.0.0", 7000);
        SentinelCommandExecutor sentinelCommandExecutor = new SentinelCommandExecutor(List.of(
                new GetMasterAddressSentinelCommandExecutionStrategy(
                        localInetSocketAddressExtractor
                )
        ));
        when(localInetSocketAddressExtractor.extractLocalSocketAddress(any()))
                .thenReturn(inetSocketAddress);
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new InboundClientRedisRequestHandler(
                sentinelCommandExecutor
        ));
        ClientRequest clientRequest =
                new ClientRequest("SENTINEL", List.of("GET-MASTER-ADDR-BY-NAME", "my-master"));
        embeddedChannel.writeInbound(clientRequest);
        assertThat(embeddedChannel.<RespDataType>readOutbound()).isEqualTo(new RespArray(List.of(
                new RespBulkString("172.31.0.0"),
                new RespBulkString(String.valueOf(7000))
        )));

    }

}