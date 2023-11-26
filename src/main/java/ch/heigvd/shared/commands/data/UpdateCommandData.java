package ch.heigvd.shared.commands.data;

import ch.heigvd.shared.game.GameState;

import java.io.Serializable;

public record UpdateCommandData(GameState gameState) implements Serializable {
}
