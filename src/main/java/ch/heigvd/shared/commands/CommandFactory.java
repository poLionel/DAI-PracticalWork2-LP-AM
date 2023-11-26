package ch.heigvd.shared.commands;

import ch.heigvd.shared.commands.data.*;
import ch.heigvd.shared.game.GameState;

public class CommandFactory {
    public static Command InvalidCommand(String message) {
        return new Command(CommandType.InvalidCommand, new InvalidCommandData(message));
    }

    public static Command JoinCommand() {
        return new Command(CommandType.Join, new JoinCommandData());
    }

    public static Command AcceptCommand(String clientID) {
        return new Command(CommandType.Accept, new AcceptCommandData(clientID));
    }

    public static Command RefuseCommand(String message) {
        return new Command(CommandType.Refuse, new RefuseCommandData(message));
    }

    public static Command UpdateCommand(GameState gameState) {
        return new Command(CommandType.Update, new UpdateCommandData(gameState));
    }

    public static Command PlaceCommand(int position) {
        return new Command(CommandType.Place, new PlaceCommandData(position));
    }

    public static Command FFCommand() {
        return new Command(CommandType.FF, new FFCommandData());
    }

    public static Command QuitCommand() {
        return new Command(CommandType.Quit, new QuitCommandData());
    }
}
