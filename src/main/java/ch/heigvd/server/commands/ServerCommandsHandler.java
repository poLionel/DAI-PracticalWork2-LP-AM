package ch.heigvd.server.commands;

import ch.heigvd.shared.commands.Command;

public class ServerCommandsHandler {
    public static Command handle(Command command) {

        Command outputCommand = null;

        switch (command.type)
        {
            case InvalidCommand -> {}
        }

        return outputCommand;
    }
}
