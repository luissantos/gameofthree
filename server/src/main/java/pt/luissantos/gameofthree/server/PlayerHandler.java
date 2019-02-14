package pt.luissantos.gameofthree.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import pt.luissantos.gameofthree.Game;
import pt.luissantos.gameofthree.common.messages.GameError;
import pt.luissantos.gameofthree.common.messages.GameMessage;
import pt.luissantos.gameofthree.common.messages.PlayMessage;
import pt.luissantos.gameofthree.services.GameService;

import java.util.Optional;

import static pt.luissantos.gameofthree.server.HttpUtils.getQueryParam;
import static pt.luissantos.gameofthree.server.NettyUtils.*;

public class PlayerHandler extends SimpleChannelInboundHandler<GameMessage> {

    public GameService gameService;

    public PlayerHandler(GameService gameService) {
        super();
        this.gameService = gameService;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof HandshakeComplete) {

            Optional<String> gameId = getQueryParam((HandshakeComplete)evt,"game");

            if(gameId.isPresent()){

                synchronized (gameService) {

                    Game game = gameService.connectToGame(gameId.get(), ctx.channel());
                    if (game.isGameReadyToStart()) {
                        game.start();
                    }
                }

            }else{
                writeAndFlushAndClose(ctx,new GameError("Game not found"));
            }

        }

        super.userEventTriggered(ctx, evt);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GameMessage msg) throws Exception {

        if (msg instanceof PlayMessage) {
            gameService.getGame(getGameId(ctx)).ifPresent(game ->
                    game.play(ctx.channel(), (PlayMessage) msg)
            );
        }

    }
}
