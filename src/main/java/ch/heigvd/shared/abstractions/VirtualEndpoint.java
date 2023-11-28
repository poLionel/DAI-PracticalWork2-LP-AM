package ch.heigvd.shared.abstractions;

import ch.heigvd.shared.commands.Command;

public interface VirtualEndpoint {
    boolean sendCommand(Command command);
}
