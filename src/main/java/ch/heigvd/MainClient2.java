package ch.heigvd;

import ch.heigvd.client.net.GameClient;
import ch.heigvd.shared.logs.Logger;

public class MainClient2 {
    public static void main(String[] args) {

        try {
            GameClient client = new GameClient("127.0.0.1");
            client.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}