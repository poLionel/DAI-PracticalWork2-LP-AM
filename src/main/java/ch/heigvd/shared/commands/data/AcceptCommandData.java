package ch.heigvd.shared.commands.data;

import ch.heigvd.shared.game.GameState;

import java.io.Serializable;

public class AcceptCommandData implements Serializable {

    private final GameState newState;

    public AcceptCommandData(GameState newState) {
        this.newState = newState;
    }

    public GameState getNewGameState() {
        return newState;
    }
}
