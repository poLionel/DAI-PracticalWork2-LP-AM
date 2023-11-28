package ch.heigvd.picocli;

import ch.heigvd.server.net.GameServer;
import ch.heigvd.shared.logs.Logger;
import picocli.CommandLine;

@CommandLine.Command(name = "Server", version = "server 0.1", mixinStandardHelpOptions = true)
public class PicocliServerCommand implements Runnable{

    @CommandLine.Option(names = {"-p", "--port"}, description = "The port of the server", required = false)
    private short port = GameServer.DEFAULT_PORT;

    @Override
    public void run() {
        boolean wasSuccessfull = executeServer();
    }

    public boolean executeServer(){
        try{
            Logger.setEnabled();
            GameServer server = new GameServer(port);
            server.start();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }
}
