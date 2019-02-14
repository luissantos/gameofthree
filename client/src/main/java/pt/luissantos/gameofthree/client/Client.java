package pt.luissantos.gameofthree.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.luissantos.gameofthree.common.messages.GameMessage;

import java.net.URI;
import java.net.URISyntaxException;


public class Client {

    static Logger logger = LoggerFactory.getLogger(Client.class);

    public GameMessage run(String  url) throws URISyntaxException, InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        URI uri = new URI(url);
        GameMessageHandler gameHandler = new GameMessageHandler();

        try {

            logger.info("Connecting to: {}",uri);

            io.netty.channel.ChannelInitializer channelInitializer = new ChannelInitializerImpl(gameHandler,uri);
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(channelInitializer);

            Channel ch = b.connect(uri.getHost(), uri.getPort()).sync().channel();
            ((ChannelInitializerImpl) channelInitializer).getHandler().handshakeFuture().sync();

            ch.closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }

        return gameHandler.getResult();
    }
}
