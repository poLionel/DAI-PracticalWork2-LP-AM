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

    private final VirtualClient virtualClient;
    private final GameUI gameUI = GameUI.getInstance();

    public ClientCommandsHandler(VirtualClient virtualClient) {
        this.virtualClient = virtualClient;
    }

    public void handle(Command command) {

        try {
            switch (command.type) {
                case Accept: HandleAcceptCommand((AcceptCommandData)command.value);
                    break;
                case Refuse: HandleRefuseCommand((RefuseCommandData)command.value);
                    break;
                case InvalidCommand: HandleInvalidCommand((InvalidCommandData)command.value);
                    break;
                case Update: HandleUpdateCommand((UpdateCommandData)command.value);
                    break;
                default: // If we received wrong command from the server ignore
                    break;
            };
        }
        catch (ClassCastException ex) {
            //todo
        }
    }

    public void sendInitialCommand() {
        virtualClient.sendCommand(CommandFactory.JoinCommand());
    }

    private void HandleAcceptCommand(AcceptCommandData data) {
        // The accept command contains our client ID
        Logger.log(String.format("Recieved client ID %s", data.clientID()), this, LogLevel.Information);
        virtualClient.setClientID(data.clientID());
    }

    private void HandleRefuseCommand(RefuseCommandData data) {
        Logger.log(data.message(), LogLevel.Warning);
        gameUI.displayMessage(data.message());
    }

    private void HandleUpdateCommand(UpdateCommandData data) {

        GameState gameState = data.gameState();
        String clientID = virtualClient.getClientID();
        gameUI.displayGameState(gameState, clientID);

        if(gameState.canPlay(clientID)) {

            int input = gameUI.getInput(gameState, clientID);

            //Checks if FF
            if(input == 1515)
                virtualClient.sendCommand(CommandFactory.FFCommand());
            else
                virtualClient.sendCommand(CommandFactory.PlaceCommand(input));
        }
    }

    private void HandleInvalidCommand(InvalidCommandData data) {
        Logger.log(data.message(), LogLevel.Error);
        gameUI.displayMessage(data.message());
    }
}