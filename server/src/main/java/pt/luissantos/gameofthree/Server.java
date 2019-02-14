package pt.luissantos.gameofthree;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.luissantos.gameofthree.server.WebSocketServerInitializer;
import pt.luissantos.gameofthree.services.GameService;

import java.security.SecureRandom;

public class Server {

    Logger logger = LoggerFactory.getLogger(Server.class);

    GameService gameService = new GameService(new SecureRandom());

    public void run(int port) throws InterruptedException {


        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketServerInitializer(gameService));

            Channel ch = b.bind(port).sync().channel();

            logger.info("Open your web browser and navigate to " + "http://127.0.0.1:" + port + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }

    }
}
