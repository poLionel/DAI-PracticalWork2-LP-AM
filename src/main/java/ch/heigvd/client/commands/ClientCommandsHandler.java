package ch.heigvd.client.commands;

import ch.heigvd.shared.commands.Command;

public class ClientCommandsHandler {
    public static Command handle(Command command) {

        Command outputCommand = null;

        switch (command.type)
        {
            case InvalidCommand -> {}
        }

        return outputCommand;
    }
}
