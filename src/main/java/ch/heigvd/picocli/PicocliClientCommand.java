package ch.heigvd.picocli;

import ch.heigvd.client.net.GameClient;
import picocli.CommandLine;

@CommandLine.Command(name = "Client", version = "client 0.1", mixinStandardHelpOptions = true)
public class PicocliClientCommand implements Runnable{

    @Override
    public void run(){
        boolean wasSuccessful = executeClient();
    }

    public boolean executeClient(){
        try{
            GameClient client = new GameClient("127.0.0.1");
            client.start();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

}
