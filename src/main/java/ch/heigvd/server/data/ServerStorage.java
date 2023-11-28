package ch.heigvd.server.data;

import ch.heigvd.shared.abstractions.VirtualClient;
import ch.heigvd.shared.commands.CommandFactory;
import ch.heigvd.shared.game.GameState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ServerStorage implements Serializable {
    private static ServerStorage instance = null;
    private final GameState game = new GameState();
    private final List<VirtualClient> clients = Collections.synchronizedList(new ArrayList<>());

    private ServerStorage() {
        game.restartGame();
    }

    public static synchronized ServerStorage getInstance() {
        if (instance == null)
            instance = new ServerStorage();

        return instance;
    }

    public GameState getGameState() {
        return game;
    }

    public synchronized boolean subscribeClient(VirtualClient virtualClient) {
        return clients.add(virtualClient);
    }

    public synchronized boolean unsubscribeClient(VirtualClient virtualClient) {
        return clients.remove(virtualClient);
    }

    public synchronized void notifyClients() {
        for (VirtualClient client : clients)
            client.sendCommand(CommandFactory.UpdateCommand(game));
    }
}
