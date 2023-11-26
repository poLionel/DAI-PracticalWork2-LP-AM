package ch.heigvd.client.ui;

import ch.heigvd.shared.game.GameState;
import ch.heigvd.shared.game.PlayerState;

public class GameUI {
    private static GameUI instance = null;

    private GameUI() {
    }

    public static synchronized GameUI getInstance() {
        if (instance == null)
            instance = new GameUI();

        return instance;
    }

    public void displayGameState(GameState gameState) {
        PlayerState[] players = gameState.getPlayers();
        for (PlayerState player : players) {
            System.out.println(player.ID);
        }
    }
}
