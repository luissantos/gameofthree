package pt.luissantos.gameofthree.common.messages;

import java.util.Objects;

public class GameEnded implements GameMessage {

    public static final GameEnded WIN = new GameEnded("You win!");
    public static final GameEnded LOSE = new GameEnded("You lose!");

    private final String message;

    GameEnded(String message) {
        this.message = message;
    }

    @Override
    public String getRawMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEnded that = (GameEnded) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
