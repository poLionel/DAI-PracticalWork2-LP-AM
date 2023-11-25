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

    public static Command AcceptCommand(GameState gameState) {
        return new Command(CommandType.Accept, new AcceptCommandData(gameState));
    }

    public static Command RefuseCommand() {
        return new Command(CommandType.Refuse, new RefuseCommandData());
    }

    public static Command PlaceCommand() {
        return new Command(CommandType.Place, new PlaceCommandData());
    }

    public static Command FFCommand() {
        return new Command(CommandType.FF, new FFCommandData());
    }

    public static Command QuitCommand() {
        return new Command(CommandType.Quit, new QuitCommandData());
    }
}
