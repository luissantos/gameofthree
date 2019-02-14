package pt.luissantos.gameofthree.common.messages;

public class PlayMessage implements GameMessage {

    Integer playerInput;


    public PlayMessage(Integer playerInput) {
        this.playerInput = playerInput;
    }

    @Override
    public String getRawMessage() {
        return playerInput.toString();
    }


    public Integer getPlayerInput() {
        return playerInput;
    }
}
