package pt.luissantos.gameofthree.client;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import pt.luissantos.gameofthree.common.decoders.GameMessageDecoder;
import pt.luissantos.gameofthree.common.encoders.GameMessageEncoder;

import java.net.URI;

public class ChannelInitializerImpl extends io.netty.channel.ChannelInitializer<SocketChannel> {

    private WebSocketClientHandler handler;

    private URI uri;

    GameMessageHandler gameHandler;

    public ChannelInitializerImpl(GameMessageHandler gameHandler, URI uri) {
        this.uri = uri;
        handler = new WebSocketClientHandler(
                    WebSocketClientHandshakerFactory.newHandshaker(
                        uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));
        this.gameHandler = gameHandler;
    }

    public WebSocketClientHandler getHandler() {
        return handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpClientCodec());
        p.addLast(new HttpObjectAggregator(8192));
        p.addLast(handler);
        p.addLast(new GameMessageEncoder());
        p.addLast(new GameMessageDecoder());
        p.addLast(gameHandler);
    }
}
