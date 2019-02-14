package pt.luissantos;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;
import pt.luissantos.gameofthree.Game;
import pt.luissantos.gameofthree.common.messages.GameMessage;
import pt.luissantos.gameofthree.common.messages.PlayMessage;

import java.util.Random;

public class GameTest {


    @Test
    public void testThatWhen1stPlayerIsRegisteredRegisterReturnsFalse() {

        Game game = new Game(new Random(0));

        Assert.assertFalse(game.registerPlayer(new EmbeddedChannel()));


    }

    @Test
    public void testThatWhenAllThePlayersAreInTheGameRegisterSucceeds() {
        Game game = new Game(new Random(0));

        game.registerPlayer(new EmbeddedChannel());

        Assert.assertTrue(game.registerPlayer(new EmbeddedChannel()));
    }

    @Test(expected = RuntimeException.class)
    public void testThatIfMoreThan2PlayersAreRegisteredAndExceptionIsThrown() {
        Game game = new Game(new Random(0));
        game.registerPlayer(new EmbeddedChannel());
        game.registerPlayer(new EmbeddedChannel());
        game.registerPlayer(new EmbeddedChannel());
    }

    @Test
    public void testCase56PlayerOneWins() {

        Random random = new Random(1);
        random.nextInt(2);

        Game game = new Game(random);

        EmbeddedChannel player1 = new EmbeddedChannel();
        EmbeddedChannel player2 = new EmbeddedChannel();

        game.registerPlayer(player1);
        game.registerPlayer(player2);

        game.start();
        discardOutboundMessages(player1,player2);

        game.play(player1,new PlayMessage(56));
        discardOutboundMessages(player1,player2);

        game.play(player2,new PlayMessage(19));
        discardOutboundMessages(player1,player2);

        game.play(player1,new PlayMessage(6));
        discardOutboundMessages(player1,player2);

        game.play(player2,new PlayMessage(2));

        GameMessage msg1 = player1.readOutbound();
        GameMessage msg2 = player2.readOutbound();

        Assert.assertEquals("You win!",msg1.getRawMessage());
        Assert.assertEquals("You lose!",msg2.getRawMessage());

    }

    private void discardOutboundMessages(EmbeddedChannel ...channels){
        for(EmbeddedChannel c : channels){
            c.releaseOutbound();
        }
    }
}
