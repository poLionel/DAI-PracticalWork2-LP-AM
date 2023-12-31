package ch.heigvd.server.net;

import ch.heigvd.shared.logs.LogLevel;
import ch.heigvd.shared.logs.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    public static final int DEFAULT_PORT = 5187;
    private static final int MAX_THREADS_COUNT = 10;
    private final int port;
    private boolean askedForClosing;

    public GameServer() {
        this.port = DEFAULT_PORT;
    }
    public GameServer(int port) {
        this.port = port;
    }

    public void start() {

        Logger.log(String.format("Starting server on port %s", port), this, LogLevel.Information);
        ExecutorService executor = null;

        try(
                ServerSocket serverSocket = new ServerSocket(port);
        ) {
            executor = Executors.newFixedThreadPool(MAX_THREADS_COUNT);

            while (!askedForClosing) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(new ClientHandler(clientSocket));
            }
        }
        catch (IOException exception) {
            Logger.log(String.format("Handled error : %s", exception.getMessage()), this, LogLevel.Error);
        }
        finally {
            if(executor != null) executor.shutdown();
            Logger.log("Shutting down server", this, LogLevel.Information);
            askedForClosing = false;
        }
    }

    public void shutdown() {
        askedForClosing = true;
    }
}
