package ch.heigvd.picocli.commands;

import picocli.CommandLine;

@CommandLine.Command(name = "4 in a row",
        subcommands = { PicocliServerCommand.class, PicocliClientCommand.class, CommandLine.HelpCommand.class },
        description = "Start a server or client to play 4 in a row")
public class AppBaseCommand {
}
