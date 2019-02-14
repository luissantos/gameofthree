package pt.luissantos.gameofthree.common.messages;

public class GameError implements GameMessage {

    String error;

    public GameError(String error) {
        this.error = error;
    }

    @Override
    public String getRawMessage() {
        return "Error while connecting. Error: "+error;
    }
}
