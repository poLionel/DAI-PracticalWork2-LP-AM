package ch.heigvd.picocli.commands;

import picocli.CommandLine;

@CommandLine.Command(name = "4 in a row",
        subcommands = { PicocliServerCommand.class, PicocliClientCommand.class, CommandLine.HelpCommand.class },
        description = "Converts from or to Json for the given file format")
public class AppBaseCommand {
}
