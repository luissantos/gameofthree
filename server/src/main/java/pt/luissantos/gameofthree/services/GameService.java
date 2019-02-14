package pt.luissantos.gameofthree.services;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.luissantos.gameofthree.Game;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;

import static pt.luissantos.gameofthree.server.NettyUtils.getGameId;
import static pt.luissantos.gameofthree.server.NettyUtils.setGameId;


public class GameService {

    private static Logger logger = LoggerFactory.getLogger(GameService.class);

    private final ConcurrentMap<String, Game> games = PlatformDependent.newConcurrentHashMap();

    private Random random;

    public GameService(Random random) {
        this.random = random;
    }

    public Game connectToGame(String gameId, Channel channel) {

        Game game = games.computeIfAbsent(gameId, s -> new Game(random));

        setGameId(channel, gameId);

        game.registerPlayer(channel);

        channel.closeFuture().addListener((ChannelFutureListener) future -> {

            String gid = getGameId(future.channel());

            games.computeIfPresent(gid, (s, g) -> {
                if (!g.unregisterPlayer(future.channel())) {
                    return g;
                }
                logger.info("Game {} finished", gid);
                return null;
            });


            games.computeIfPresent(gid, (s, g) -> {
                g.stop();
                return g;
            });
        });

        return game;
    }

    public Optional<Game> getGame(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

}
