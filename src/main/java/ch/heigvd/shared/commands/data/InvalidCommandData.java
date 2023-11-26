package ch.heigvd.shared.commands.data;

import java.io.Serializable;

public record InvalidCommandData(String message) implements Serializable {
}
