package ch.heigvd.shared.commands.data;

import java.io.Serializable;

public record AcceptCommandData(String clientID) implements Serializable {
}
