package ch.heigvd.picocli;

import ch.heigvd.server.net.GameServer;
import ch.heigvd.shared.logs.Logger;
import picocli.CommandLine;

@CommandLine.Command(name = "Server", version = "server 0.1", mixinStandardHelpOptions = true)
public class PicocliServerCommand implements Runnable{

    @Override
    public void run() {
        boolean wasSuccessfull = executeServer();
    }

    public boolean executeServer(){
        try{
            Logger.setEnabled();
            GameServer server = new GameServer();
            server.start();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }
}
