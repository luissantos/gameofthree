package pt.luissantos.gameofthree.common.messages;

public class InvalidPlay implements GameMessage {

    @Override
    public String getRawMessage() {
        return "Invalid play";
    }
}
