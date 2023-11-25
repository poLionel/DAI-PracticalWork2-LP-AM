package ch.heigvd.server.commands;

import ch.heigvd.shared.abstractions.VirtualClient;
import ch.heigvd.shared.commands.Command;
import ch.heigvd.shared.commands.CommandFactory;
import ch.heigvd.shared.commands.data.*;
import ch.heigvd.shared.game.GameState;

import java.util.UUID;

public class ServerCommandsHandler {

    private static final GameState gameState = new GameState();
    private final VirtualClient virtualClient;

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

            virtualClient.sendCommand(outputCommand);
        }
        catch (ClassCastException ex) {
            virtualClient.sendCommand(CommandFactory.InvalidCommand("The server was unable to deserialize the command data"));
        }
    }

    private Command HandleJoinCommand(JoinCommandData data) {
        if(gameState.CanAddPlayer()) {
            String clientID = UUID.randomUUID().toString();
            virtualClient.setClientID(clientID);
            return CommandFactory.AcceptCommand(gameState);
        }
        else {
            return CommandFactory.RefuseCommand();
        }
    }

    private Command HandlePlaceCommand(PlaceCommandData data) {
        return null;
    }

    private Command HandleFFCommand(FFCommandData data) {
        return null;
    }

    private Command HandleQuitCommand(QuitCommandData data) {
        return null;
    }
}
