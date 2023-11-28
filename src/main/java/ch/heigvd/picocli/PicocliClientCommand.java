package ch.heigvd.picocli;

import ch.heigvd.client.net.GameClient;
import ch.heigvd.server.net.GameServer;
import picocli.CommandLine;

@CommandLine.Command(name = "client", version = "client 0.1", mixinStandardHelpOptions = true)
public class PicocliClientCommand implements Runnable{

    @CommandLine.Option(names = {"-p", "--port"}, description = "The port of the server", required = false)
    private short port = GameServer.DEFAULT_PORT;

    @CommandLine.Option(names = {"-a", "--address"}, description = "The address of the server", required = true)
    private String address;

    @Override
    public void run(){
        boolean wasSuccessful = executeClient();
    }

    public boolean executeClient(){
        try{
            GameClient client = new GameClient(address, port);
            client.start();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

}
