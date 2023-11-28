package ch.heigvd.picocli;

import picocli.CommandLine;

@CommandLine.Command(name = "4 in a row",
        subcommands = { PicocliServerCommand.class, PicocliClientCommand.class, CommandLine.HelpCommand.class },
        description = "Start a server or client to play 4 in a row")
public class AppBaseCommand {
    public static void main(String[] args) {
        AppBaseCommand app = new AppBaseCommand();
        int exitCode = new CommandLine(app).execute(args);
        System.exit(exitCode);
    }
}
