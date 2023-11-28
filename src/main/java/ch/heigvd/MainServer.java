package ch.heigvd;

import ch.heigvd.client.net.GameClient;
import ch.heigvd.server.net.GameServer;
import ch.heigvd.shared.logs.Logger;

public class MainServer {
    public static void main(String[] args) {

        try {
            Logger.setEnabled();
            GameServer server = new GameServer();
            server.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}