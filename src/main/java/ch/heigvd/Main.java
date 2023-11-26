package ch.heigvd;

import ch.heigvd.client.net.GameClient;
import ch.heigvd.server.net.ClientHandler;
import ch.heigvd.server.net.GameServer;
import ch.heigvd.shared.logs.Logger;

public class Main {
    public static void main(String[] args) {

        try {

            GameServer server = new GameServer();
            GameClient client = new GameClient("127.0.0.1");

            Thread serverThread = new Thread(server::start);
            Thread clientThread = new Thread(client::start);

            serverThread.start();
            Thread.sleep(1000);
            clientThread.start();

            clientThread.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}