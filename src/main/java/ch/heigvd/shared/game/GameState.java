package ch.heigvd.shared.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState implements Serializable {
    public static final int MAX_PLAYER_COUNT = 2;
    private final List<PlayerState> players = Collections.synchronizedList(new ArrayList<>());

    public synchronized boolean AddPlayer(String ID) {
        boolean canAddPlayer = CanAddPlayer();
        if(canAddPlayer) players.add(new PlayerState(ID));
        return canAddPlayer;
    }

    public synchronized boolean RemovePlayer(String ID) {
        boolean canRemovePlayer = CanRemovePlayer();
        if(canRemovePlayer) {
            PlayerState player = GetPlayerWithID(ID);
            if(player != null) {
                players.remove(player);
                return true;
            }
        }
        return false;
    }

    public boolean CanAddPlayer() {
        return players.size() < MAX_PLAYER_COUNT;
    }

    public boolean CanRemovePlayer() {
        return !players.isEmpty();
    }

    public boolean ContainsPlayer(String playerID) {
        return GetPlayerWithID(playerID) != null;
    }

    private synchronized PlayerState GetPlayerWithID(String ID) {
        for (PlayerState player : players)
            if(player.ID.equals(ID)) return player;
        return null;
    }
}
