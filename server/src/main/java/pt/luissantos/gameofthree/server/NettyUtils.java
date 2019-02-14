package pt.luissantos.gameofthree.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyUtils {

    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private static ClassLoader classLoader = NettyUtils.class.getClassLoader();

    private final static AttributeKey<String> GAME_ID = AttributeKey.valueOf("GAME_ID");

    public static String getGameId(Channel channel) {
        return channel.attr(GAME_ID).get();
    }

    public static String getGameId(ChannelHandlerContext ctx) {
        return getGameId(ctx.channel());
    }

    public static void setGameId(Channel channel, String gameID) {
        channel.attr(GAME_ID).set(gameID);
    }

    public static ByteBuf getPageContent(String fileName) throws IOException {
        return Unpooled.copiedBuffer(IOUtils.toByteArray(classLoader.getResourceAsStream(fileName)));
    }

    public static ChannelFuture writeAndFlush(ChannelHandlerContext ctx, Object obj) {
        return ctx.channel().writeAndFlush(obj);
    }

    public static ChannelFuture writeAndFlushAndClose(Channel channel, Object obj) {
        return channel.writeAndFlush(obj).addListener( (ChannelFutureListener)(future) -> {
                executorService.schedule( (Runnable) future.channel()::close , 2, TimeUnit.SECONDS);
        });
    }

    public static ChannelFuture writeAndFlushAndClose(ChannelHandlerContext ctx, Object obj) {
        return writeAndFlushAndClose(ctx.channel(), obj);
    }

}
