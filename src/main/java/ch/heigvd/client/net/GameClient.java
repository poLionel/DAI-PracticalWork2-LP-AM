package ch.heigvd.client.net;

import ch.heigvd.server.net.ClientHandler;
import ch.heigvd.shared.logs.LogLevel;
import ch.heigvd.shared.logs.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameClient {
    public static final int DEFAULT_PORT = 5187;
    private final int port;
    private boolean askedForClosing;

    public GameClient() {
        this.port = DEFAULT_PORT;
    }
    public GameClient(int port) {
        this.port = port;
    }

    public void StartClient() {

    }

    public void ShutdownClient() {
        askedForClosing = true;
    }
}
