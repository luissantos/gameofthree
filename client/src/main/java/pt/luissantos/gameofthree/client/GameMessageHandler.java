package pt.luissantos.gameofthree.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.luissantos.gameofthree.common.messages.GameEnded;
import pt.luissantos.gameofthree.common.messages.GameMessage;
import pt.luissantos.gameofthree.common.messages.PlayMessage;
import pt.luissantos.gameofthree.common.messages.StartGame;

import java.util.Optional;
import java.util.Random;

import static pt.luissantos.gameofthree.common.GameLogic.calculateNextNumber;

public class GameMessageHandler extends SimpleChannelInboundHandler<GameMessage> {

    private static Logger logger = LoggerFactory.getLogger(GameMessageHandler.class);

    private Random random = new Random();

    private GameMessage result;

    public GameMessage getResult() {
        return result;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GameMessage msg) throws Exception {

        if(msg instanceof StartGame){
            Integer play = random.nextInt(10000);
            ctx.writeAndFlush(new PlayMessage(play));
            logger.info("Starting game: {}",play);
        }

        if(msg instanceof PlayMessage){
            logger.info("Got: {}",((PlayMessage) msg).getPlayerInput());


            Optional<Integer> nextPlay = calculateNextNumber(((PlayMessage) msg).getPlayerInput());

            logger.info("Playing: {}",nextPlay.get());
            ctx.writeAndFlush(new PlayMessage(nextPlay.get()));
        }

        if(msg instanceof GameEnded){
            result = msg;
            logger.info("Game is over: {}",msg.getRawMessage());
        }


    }
}
