package ch.heigvd.shared.commands.data;

import java.io.Serializable;

public record RefuseCommandData(String message) implements Serializable {
}
