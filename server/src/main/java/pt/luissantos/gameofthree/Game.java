package pt.luissantos.gameofthree;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import pt.luissantos.gameofthree.common.messages.GameEnded;
import pt.luissantos.gameofthree.common.messages.InvalidPlay;
import pt.luissantos.gameofthree.common.messages.PlayMessage;
import pt.luissantos.gameofthree.common.messages.StartGame;

import java.util.Random;

import static pt.luissantos.gameofthree.common.GameLogic.calculateNextNumber;
import static pt.luissantos.gameofthree.server.NettyUtils.writeAndFlushAndClose;

public class Game {

    private Random random;

    private Channel player1;
    private Channel player2;
    private Channel nextPlayer = null;
    private Integer lastPlay = null;


    public Game(Random random) {
        this.random = random;
    }

    synchronized public boolean registerPlayer(Channel channel){
        if(player1 == null){
            player1 = channel;
            return false;
        }

        if(player2 == null){
            player2 = channel;
            return true;
        }

        throw new  RuntimeException("A game cannot have more than 2 players");
    }

    public boolean unregisterPlayer(Channel channel){

        if(nextPlayer == channel){
            nextPlayer = null;
        }

        if(player1 == channel){
            player1 = null;
        }

        if(player2 == channel){
            player2 = null;
        }

        return player1 == null && player2 == null;
    }

    public Channel start(){

        Channel randomPlayer = nextPlayer = getRandomPlayer();
        randomPlayer.writeAndFlush(StartGame.INSTANCE);

        return randomPlayer;
    }

    public void stop(){

        if(player1!=null && player1.isOpen()){
            player1.close();
        }

        if(player2!=null && player2.isOpen()){
            player2.close();
        }
    }

    public boolean isGameReadyToStart(){
        return player1 != null && player2 != null;
    }

    private Channel getRandomPlayer(){
        int startingPlayer = random.nextInt(2);
        return startingPlayer == 0 ? player1 : player2;
    }

    public void play(Channel currentPlayer, PlayMessage msg){

        if(currentPlayer != nextPlayer){
            currentPlayer.writeAndFlush(new InvalidPlay());
            return;
        }

        if(!isPlayValid(lastPlay,msg.getPlayerInput())){
            currentPlayer.writeAndFlush(new InvalidPlay());
            return;
        }

        Channel otherPlayer = otherPlayer(currentPlayer);

        if(isEndOfTheGame(msg.getPlayerInput())){
            writeAndFlushAndClose(otherPlayer, GameEnded.WIN);
            writeAndFlushAndClose(currentPlayer, GameEnded.LOSE);
            return;
        }

        lastPlay = msg.getPlayerInput();

        otherPlayer.writeAndFlush(msg).addListener((ChannelFutureListener) future -> advanceToNextPlayer(future.channel()));

    }

    private Channel otherPlayer(Channel channel){
        return player1 == channel ? player2 : player1;
    }

    private Channel advanceToNextPlayer(Channel nextPlayer){
        return this.nextPlayer = nextPlayer;
    }

    private static boolean isEndOfTheGame(Integer currentPlay){
        return isPlayValid(currentPlay,1);
    }

    private static boolean isPlayValid(Integer lastPlay, Integer currentPlay){

        // 1st play is always valid
        if(lastPlay == null){
            return true;
        }

        return calculateNextNumber(lastPlay)
                        .filter( next -> next.equals(currentPlay) ).isPresent();

    }



}
