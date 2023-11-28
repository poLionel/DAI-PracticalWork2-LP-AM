package ch.heigvd.shared.commands.data;

import java.io.Serializable;

public record UpdateCommandData(ch.heigvd.shared.game.GameState gameState) implements Serializable {
}
