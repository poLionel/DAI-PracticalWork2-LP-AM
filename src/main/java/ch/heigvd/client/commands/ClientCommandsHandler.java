package ch.heigvd.client.commands;

import ch.heigvd.client.ui.GameUI;
import ch.heigvd.shared.abstractions.VirtualClient;
import ch.heigvd.shared.commands.Command;
import ch.heigvd.shared.commands.CommandFactory;
import ch.heigvd.shared.commands.data.AcceptCommandData;
import ch.heigvd.shared.commands.data.InvalidCommandData;
import ch.heigvd.shared.commands.data.RefuseCommandData;
import ch.heigvd.shared.commands.data.UpdateCommandData;
import ch.heigvd.shared.game.GameState;
import ch.heigvd.shared.logs.LogLevel;
import ch.heigvd.shared.logs.Logger;

public class ClientCommandsHandler {

    private static GameState gameState;
    private final VirtualClient virtualClient;
    private final GameUI gameUI = GameUI.getInstance();

    public ClientCommandsHandler(VirtualClient virtualClient) {
        this.virtualClient = virtualClient;
    }

    public void handle(Command command) {

        try {
            Command outputCommand = switch (command.type) {
                case Accept -> HandleAcceptCommand((AcceptCommandData)command.value);
                case Refuse -> HandleRefuseCommand((RefuseCommandData)command.value);
                case InvalidCommand -> HandleInvalidCommand((InvalidCommandData)command.value);
                case Update -> HandleUpdateCommand((UpdateCommandData)command.value);
                default -> null;
            };

            if(outputCommand != null)
                virtualClient.sendCommand(outputCommand);
        }
        catch (ClassCastException ex) {
        }
    }

    public void sendInitialCommand() {
        virtualClient.sendCommand(CommandFactory.JoinCommand());
    }

    private Command HandleAcceptCommand(AcceptCommandData data) {
        // The accept command contains our client ID
        virtualClient.setClientID(data.clientID());
        return null;
    }

    private Command HandleRefuseCommand(RefuseCommandData data) {
        Logger.log(data.message(), LogLevel.Warning);
        return null;
    }

    private Command HandleUpdateCommand(UpdateCommandData data) {
        gameState = data.gameState();
        if(gameState.canPlay(virtualClient.getClientID()))
        {
            int position = gameUI.getPosition();
            Logger.log("Le joueur peut jouer", LogLevel.Information);
            return CommandFactory.PlaceCommand(position);
        }

        return null;
    }

    private Command HandleInvalidCommand(InvalidCommandData data) {
        Logger.log(data.message(), LogLevel.Error);
        return null;
    }
}
