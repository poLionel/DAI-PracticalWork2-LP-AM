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
            switch (command.type) {
                case Join: HandleJoinCommand((JoinCommandData) command.value);
                    break;
                case Place: HandlePlaceCommand((PlaceCommandData) command.value);
                    break;
                case FF: HandleFFCommand((FFCommandData) command.value);
                    break;
                case Quit: HandleQuitCommand((QuitCommandData) command.value);
                    break;
                default : virtualClient.sendCommand(CommandFactory.InvalidCommand("This command type is not supported by the server"));
                    break;
            };
        }
        catch (ClassCastException ex) {
            virtualClient.sendCommand(CommandFactory.InvalidCommand("The server was unable to deserialize the command data"));
        }
    }

    private void HandleJoinCommand(JoinCommandData data) {
        GameState gameState = serverStorage.getGameState();
        if(gameState.canAddPlayer()) {

            // Generate the new client ID
            String clientID = UUID.randomUUID().toString();
            virtualClient.setClientID(clientID);

            // Subscribe the client to server events
            serverStorage.subscribeClient(virtualClient);

            // Add player to the game
            gameState.addPlayer(clientID);

            // Finally accept the join and notify every client
            virtualClient.sendCommand(CommandFactory.AcceptCommand(virtualClient.getClientID()));
            serverStorage.notifyClients();
        }
        else {
            // Cannot add player to the game, return a refuse command
            virtualClient.sendCommand(CommandFactory.RefuseCommand("Cannot join the game, the game is full"));
        }
    }

    private void HandlePlaceCommand(PlaceCommandData data) {

        GameState gameState = serverStorage.getGameState();
        String clientID = virtualClient.getClientID();
        boolean canPlace = gameState.canPlace(data.position(), clientID);

        // Verify if the player is allowed to play
        if(!canPlace) {
            virtualClient.sendCommand(CommandFactory.RefuseCommand("You cannot place a pawn here"));
            return;
        }

        int placePosition = data.position();
        boolean isValidMove = gameState.canPlace(placePosition, clientID);

        // Verify if the move is valid
        if(!isValidMove) {
            virtualClient.sendCommand((CommandFactory.RefuseCommand("The position given is not valid.")));
            return;
        }

        // Place the pawn and notify every client
        gameState.place(placePosition, clientID);
        serverStorage.notifyClients();

        // If someone won the game restart
        if(gameState.getGameWinner() != null) {
            gameState.restartGame();
            serverStorage.notifyClients();
        }
    }

    private void HandleFFCommand(FFCommandData data) {
        serverStorage.getGameState().restartGame();
        System.out.println("A player has FF, game restart");
    }

    private void HandleQuitCommand(QuitCommandData data) {
        String clientID = virtualClient.getClientID();
        serverStorage.unsubscribeClient(virtualClient);
        serverStorage.getGameState().removePlayer(clientID);
        serverStorage.getGameState().restartGame();
        serverStorage.notifyClients();
    }
}
