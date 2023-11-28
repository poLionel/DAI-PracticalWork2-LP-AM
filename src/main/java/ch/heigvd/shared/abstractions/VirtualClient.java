package ch.heigvd.shared.abstractions;

import ch.heigvd.shared.commands.Command;

public interface VirtualClient extends VirtualEndpoint {
    String getClientID();

    void setClientID(String clientID);
}
