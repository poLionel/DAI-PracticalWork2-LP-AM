package ch.heigvd.shared.game;

import ch.heigvd.shared.abstractions.VirtualClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState implements Serializable {
    public static final int MAX_PLAYER_COUNT = 2;
    private final List<PlayerState> players = Collections.synchronizedList(new ArrayList<>());

    private final char[][] game = new char[6][7];

    private String playerToPlay;

    private final char player_1 = 'X';
    private final char player_2 = 'O';
    private final char emptyCase = ' ';

    public synchronized boolean addPlayer(String ID) {
        if(ID == null) return false;
        boolean canAddPlayer = canAddPlayer();
        if(canAddPlayer) {
            players.add(new PlayerState(ID));
            playerToPlay = ID;
        }
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

    public synchronized boolean canPlay(String ID){
        if(ID != null && players.size() == 2 && ID.equals(playerToPlay))
            return true;

        return false;
    }

    public synchronized boolean validPosition(int column, String player){
        boolean isValidMove = false;

        if(column >= 7 || column < 0)
            return isValidMove;

        for(int i = 0; i < 6; ++i){
            if(game[i][column] == emptyCase){
                game[i][column] = playerToPlay.equals(player) ? player_1 : player_2;
                isValidMove = true;
            }
        }

        return isValidMove;
    }

    public boolean gameWin(){

        return false;
    }
}