package ch.heigvd.server.commands;

import ch.heigvd.server.data.ServerStorage;
import ch.heigvd.shared.abstractions.VirtualClient;
import ch.heigvd.shared.commands.Command;
import ch.heigvd.shared.commands.CommandFactory;
import ch.heigvd.shared.commands.data.*;
import ch.heigvd.shared.game.GameState;

import java.util.UUID;

public class ServerCommandsHandler {

    private final VirtualClient virtualClient;
    private final ServerStorage serverStorage = ServerStorage.getInstance();

    public ServerCommandsHandler(VirtualClient virtualClient) {
        this.virtualClient = virtualClient;
    }

    public void handle(Command command) {
        try {
            Command outputCommand = switch (command.type) {
                case Join -> HandleJoinCommand((JoinCommandData) command.value);
                case Place -> HandlePlaceCommand((PlaceCommandData) command.value);
                case FF -> HandleFFCommand((FFCommandData) command.value);
                case Quit -> HandleQuitCommand((QuitCommandData) command.value);
                default -> CommandFactory.InvalidCommand("This command type is not supported by the server");
            };

            if(outputCommand != null)
                virtualClient.sendCommand(outputCommand);
        }
        catch (ClassCastException ex) {
            virtualClient.sendCommand(CommandFactory.InvalidCommand("The server was unable to deserialize the command data"));
        }
    }

    private synchronized Command HandleJoinCommand(JoinCommandData data) {
        GameState gameState = serverStorage.getGameState();
        if(gameState.canAddPlayer()) {

            // Generate the new client ID
            String clientID = UUID.randomUUID().toString();
            virtualClient.setClientID(clientID);

            // Subscribe the client to server events
            serverStorage.subscribeClient(virtualClient);

            // Add player to the game
            gameState.addPlayer(clientID);

            // Finally notify the clients and return an accept command
            serverStorage.notifyClients();
            return CommandFactory.AcceptCommand(clientID);
        }
        else {
            // Cannot add player to the game, return a refuse command
            return CommandFactory.RefuseCommand("Cannot join the game, the game is full");
        }
    }

    private Command HandlePlaceCommand(PlaceCommandData data) {
        return null;
    }

    private Command HandleFFCommand(FFCommandData data) {
        return null;
    }

    private Command HandleQuitCommand(QuitCommandData data) {
        String clientID = virtualClient.getClientID();
        serverStorage.unsubscribeClient(virtualClient);
        serverStorage.getGameState().removePlayer(clientID);
        return CommandFactory.AcceptCommand(clientID);
    }
}
