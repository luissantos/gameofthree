package pt.luissantos.gameofthree.common.messages;

public class StartGame implements GameMessage {

    public static StartGame INSTANCE = new StartGame();

    private StartGame() {
    }

    @Override
    public String getRawMessage() {
        return "START";
    }
}
