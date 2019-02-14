package pt.luissantos.gameofthree.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

import pt.luissantos.gameofthree.common.decoders.GameMessageDecoder;
import pt.luissantos.gameofthree.common.encoders.GameMessageEncoder;
import pt.luissantos.gameofthree.services.GameService;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final String WEBSOCKET_PATH = "/websocket";

    GameService gameService;

    public WebSocketServerInitializer(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true, 65536, false, true));
        pipeline.addLast(new WebSocketIndexPageHandler());
        pipeline.addLast(new GameMessageDecoder());
        pipeline.addLast(new PlayerHandler(gameService));
        pipeline.addLast(new GameMessageEncoder());
    }
}