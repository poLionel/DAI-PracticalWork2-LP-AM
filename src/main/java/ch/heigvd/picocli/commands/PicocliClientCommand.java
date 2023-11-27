package ch.heigvd.picocli.commands;

import picocli.CommandLine;
@CommandLine.Command(name = "Client", version = "client 0.1", mixinStandardHelpOptions = true)
public class PicocliClientCommand implements Runnable{

    @Override
    public void run(){}

}
