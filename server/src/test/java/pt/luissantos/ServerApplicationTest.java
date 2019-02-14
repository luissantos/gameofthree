
package pt.luissantos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.luissantos.gameofthree.Server;
import pt.luissantos.gameofthree.client.Client;
import pt.luissantos.gameofthree.common.messages.GameEnded;
import pt.luissantos.gameofthree.common.messages.GameMessage;

import java.util.Random;
import java.util.concurrent.*;

public class ServerApplicationTest {

    ExecutorService executor = Executors.newCachedThreadPool();


    Integer port = new Random().ints(8000,10000).findFirst().getAsInt();

    String gameURL;

    @Before
    public void setUp() throws Exception {
        gameURL = "ws://127.0.0.1:"+port+"/websocket?game="+port;
    }

    @Test
    public void testThatThereIsAlwaysAWinnerAndALoser() throws ExecutionException, InterruptedException {

        Server server = new Server();

        Client client1 = new Client();

        Client client2 = new Client();


        Future fserver = executor.submit(() -> {
            try {
                server.run(port);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Future<GameMessage> f1 = executor.submit(() -> client1.run(gameURL));

        Future<GameMessage> f2 = executor.submit(() -> client2.run(gameURL));


        GameMessage m1 = f1.get();
        GameMessage m2 = f2.get();

        Assert.assertNotEquals(m1.getRawMessage(),m2.getRawMessage());

        Assert.assertTrue(m1.equals(GameEnded.WIN) || m1.equals(GameEnded.LOSE));
        Assert.assertTrue(m2.equals(GameEnded.WIN) || m2.equals(GameEnded.LOSE));
    }
}
