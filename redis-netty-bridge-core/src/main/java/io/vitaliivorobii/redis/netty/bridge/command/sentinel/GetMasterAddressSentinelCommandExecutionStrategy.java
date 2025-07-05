package io.vitaliivorobii.redis.netty.bridge.command.sentinel;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.CommandExecutionStrategy;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespBulkString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class GetMasterAddressSentinelCommandExecutionStrategy implements CommandExecutionStrategy<List<String>> {
    private static final Logger log = LoggerFactory.getLogger(GetMasterAddressSentinelCommandExecutionStrategy.class);

    public static final String GET_MASTER_ADDR_BY_NAME = "GET-MASTER-ADDR-BY-NAME";

    private final LocalInetSocketAddressExtractor localInetSocketAddressExtractor;

    public GetMasterAddressSentinelCommandExecutionStrategy(
            LocalInetSocketAddressExtractor localInetSocketAddressExtractor
    ) {
        this.localInetSocketAddressExtractor = localInetSocketAddressExtractor;
    }

    public GetMasterAddressSentinelCommandExecutionStrategy() {
        this(ctx -> (InetSocketAddress) ctx.channel().localAddress());
    }

    @Override
    public boolean canHandle(ChannelHandlerContext channelHandlerContext, List<String> args) {
        return !args.isEmpty() && args.getFirst().equalsIgnoreCase(GET_MASTER_ADDR_BY_NAME);
    }

    @Override
    public void execute(ChannelHandlerContext channelContext, List<String> arguments) {
        var socketAddress = localInetSocketAddressExtractor.extractLocalSocketAddress(channelContext);
        log.info("My local address is {}", socketAddress);
        channelContext.writeAndFlush(new RespArray(List.of(
                new RespBulkString(socketAddress.getHostString()),
                new RespBulkString(String.valueOf(socketAddress.getPort()))
        )));
    }
}
