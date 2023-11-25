package ch.heigvd.server.commands;

import ch.heigvd.shared.commands.Command;
import ch.heigvd.shared.commands.InvalidCommandData;

public class ServerCommandsHandler {
    public static Command handle(Command command) {

        Command outputCommand = null;

        switch (command.type)
        {
            case InvalidCommand:
                InvalidCommandData data = (InvalidCommandData)command.value;
        }

        return outputCommand;
    }
}
