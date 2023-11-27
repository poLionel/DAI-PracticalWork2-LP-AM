package ch.heigvd.server.commands;

import ch.heigvd.server.data.ServerStorage;
import ch.heigvd.shared.abstractions.VirtualClient;
import ch.heigvd.shared.commands.Command;
import ch.heigvd.shared.commands.CommandFactory;
import ch.heigvd.shared.commands.data.*;
import ch.heigvd.shared.game.GameState;

import java.util.UUID;

/**
 * Class that handles the server commands.
 */
public class ServerCommandsHandler {

    /**
     * The client that has sent the command.
     */
    private final VirtualClient virtualClient;

    /**
     * An object that contains information about the game and the players.
     */
    private final ServerStorage serverStorage = ServerStorage.getInstance();

    /**
     * Constructor
     */
    public ServerCommandsHandler(VirtualClient virtualClient) {
        this.virtualClient = virtualClient;
    }

    /**
     * Todo
     *
     * @param command
     */
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

    /**
     * todo
     * @param data
     * @return
     */
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
            virtualClient.sendCommand(CommandFactory.AcceptCommand(virtualClient.getClientID()));
            serverStorage.notifyClients();
            return null;
        }
        else {
            // Cannot add player to the game, return a refuse command
            return CommandFactory.RefuseCommand("Cannot join the game, the game is full");
        }
    }

    /**
     * todo
     * @param data
     * @return
     */
    private Command HandlePlaceCommand(PlaceCommandData data) {
        boolean isValidMove = serverStorage.getGameState().validPosition(data.position(), virtualClient.getClientID());

        serverStorage.notifyClients();
        return isValidMove ? null : CommandFactory.RefuseCommand("Invalid Position");
    }

    private Command HandleFFCommand(FFCommandData data) {
        serverStorage.getGameState().resetGameGrid();
        System.out.println("A player has FF, game restart");
        return null;
    }

    /**
     * Todo
     * @param data
     * @return
     */
    private Command HandleQuitCommand(QuitCommandData data) {
        String clientID = virtualClient.getClientID();
        serverStorage.unsubscribeClient(virtualClient);
        serverStorage.getGameState().removePlayer(clientID);
        return CommandFactory.AcceptCommand(clientID);
    }
}
