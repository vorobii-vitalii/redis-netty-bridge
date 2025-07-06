package io.vitaliivorobii.redis.netty.bridge.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class OrderedRequestHandlerTest {
    private static final ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    @AfterAll
    static void closeExecutor() {
        scheduledExecutorService.shutdownNow();
    }

    @Test
    void shouldEnsureStrictProcessingOrder() throws InterruptedException {
        CountDownLatch latch =  new CountDownLatch(2);
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new OrderedRequestHandler(),
                new TestInboundHandler(latch)
        );
        embeddedChannel.writeInbound(200);
        embeddedChannel.writeInbound(50);
        latch.await();
        assertThat(embeddedChannel.<Integer>readOutbound()).isEqualTo(200);
        assertThat(embeddedChannel.<Integer>readOutbound()).isEqualTo(50);
    }

    private static class TestInboundHandler extends SimpleChannelInboundHandler<Integer> {
        private final CountDownLatch latch;

        public TestInboundHandler(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, Integer msg) {
            scheduledExecutorService.schedule(() -> {
                ctx.writeAndFlush(msg);
                latch.countDown();
                System.out.println("task finished");
            }, msg, TimeUnit.MILLISECONDS);
        }
    }

}