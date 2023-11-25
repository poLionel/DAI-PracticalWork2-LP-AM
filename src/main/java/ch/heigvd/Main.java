package ch.heigvd;

import ch.heigvd.server.net.ClientHandler;
import ch.heigvd.server.net.GameServer;

public class Main {
    public static void main(String[] args) {
        GameServer server = new GameServer();
        ClientHandler client = new ClientHandler();
    }
}