package ch.heigvd.picocli.commands;

import ch.heigvd.server.net.GameServer;
import picocli.CommandLine;

@CommandLine.Command(name = "Server", version = "server 0.1", mixinStandardHelpOptions = true)
public class PicocliServerCommand implements Runnable{

    @Override
    public void run() {
        boolean wasSuccessfull = executeServer();
    }

    public boolean executeServer(){
        try{
            GameServer server = new GameServer();
            server.start();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
