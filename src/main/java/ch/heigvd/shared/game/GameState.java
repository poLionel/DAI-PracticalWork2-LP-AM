package ch.heigvd.shared.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState implements Serializable {
    public static final int MAX_PLAYER_COUNT = 2;
    private final List<PlayerState> players = Collections.synchronizedList(new ArrayList<>());

    public synchronized boolean addPlayer(String ID) {
        if(ID == null) return false;
        boolean canAddPlayer = canAddPlayer();
        if(canAddPlayer) players.add(new PlayerState(ID));
        return canAddPlayer;
    }

    public synchronized boolean removePlayer(String ID) {
        if(ID == null) return false;
        boolean canRemovePlayer = canRemovePlayer();
        if(canRemovePlayer) {
            PlayerState playerToRemove = getPlayerWithID(ID);
            if(playerToRemove != null) {
                return players.remove(playerToRemove);
            }
        }
        return false;
    }

    public PlayerState[] getPlayers() {
        PlayerState[] copy = new PlayerState[players.size()];
        players.toArray(copy);
        return copy;
    }

    public boolean canAddPlayer() {
        return players.size() < MAX_PLAYER_COUNT;
    }

    public boolean canRemovePlayer() {
        return !players.isEmpty();
    }

    public boolean containsPlayer(String playerID) {
        return getPlayerWithID(playerID) != null;
    }

    private synchronized PlayerState getPlayerWithID(String ID) {
        if(ID == null) return null;
        for (PlayerState player : players)
            if(player.ID.equals(ID)) return player;
        return null;
    }
}
