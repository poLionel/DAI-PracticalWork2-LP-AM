package ch.heigvd.picocli.commands;

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
            Thread clientThread = new Thread(client::start);

            clientThread.start();

            clientThread.join();

            return true;

        }catch (Exception e){
            //todo something
            return false;
        }
    }

}
