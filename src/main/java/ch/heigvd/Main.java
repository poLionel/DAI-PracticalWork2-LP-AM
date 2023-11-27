package ch.heigvd;

import ch.heigvd.client.net.GameClient;
import ch.heigvd.server.net.ClientHandler;
import ch.heigvd.server.net.GameServer;
import ch.heigvd.shared.logs.Logger;
import picocli.CommandLine;
import ch.heigvd.picocli.commands.AppBaseCommand;

public class Main {
    public static void main(String[] args) {

        //int exitCode = new CommandLine(new AppBaseCommand()).execute(args);
        //System.exit(exitCode);

        try {
            Logger.setEnabled();
            GameServer server = new GameServer();
            GameClient client = new GameClient("127.0.0.1");
            GameClient client2 = new GameClient("127.0.0.1");

            Thread serverThread = new Thread(server::start);
            Thread clientThread = new Thread(client::start);
            Thread clientThread2 = new Thread(client2::start);

            serverThread.start();
            Thread.sleep(1000);
            clientThread.start();
            clientThread2.start();

            clientThread.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}