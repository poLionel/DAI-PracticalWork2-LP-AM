package ch.heigvd.shared.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the rules and the logics of the game.
 */
public class GameState implements Serializable {

    /** The maximum player in the game. */
    public static final int PLAYER_COUNT = 2;

    /** The game grid width. */
    public static final int GRID_WIDTH = 7;

    /** The game grid height. */
    public static final int GRID_HEIGHT = 6;

    /** The first grid rows and column index. */
    public static final int GRID_START_INDEX = 1;

    /** The game line size required to win */
    public static final int WIN_ROW_SIZE = 4;

    /** The index that indicates that a square is empty */
    public static final int EMPTY_SQUARE_INDEX = -1;

    /** A list of the player. */
    private final List<PlayerState> players = Collections.synchronizedList(new ArrayList<>());

    /** The grid of the game. */
    private int[][] gameGrid;

    /** the player that has to play.*/
    public int playerIndexToPlay;

    /**
     * Adds a player to the array of player.
     *
     * @param ID the ID of the added player.
     * @return true if the player was added.
     */
    public synchronized boolean addPlayer(String ID) {
        if(ID == null) return false;
        boolean canAddPlayer = canAddPlayer();
        if(canAddPlayer) {
            players.add(new PlayerState(ID));
        }
        return canAddPlayer;
    }

    /**
     * Removes a player in the array of players.
     *
     * @param ID the ID of the removed player.
     * @return true if the player was removed.
     */
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

    public synchronized boolean place(int column, String playerID){
        if(!canPlace(column, playerID))
            return false;

        for(int i = 0; i < GRID_HEIGHT; ++i) {
            if(gameGrid[column - GRID_START_INDEX][i] == EMPTY_SQUARE_INDEX) {
                gameGrid[column - GRID_START_INDEX][i] = getPlayerIndex(playerID);
                playerIndexToPlay = (playerIndexToPlay + 1) % PLAYER_COUNT;
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the game can add a player.
     *
     * @return true if the game can add a player.
     */
    public boolean canAddPlayer() {
        return players.size() < PLAYER_COUNT;
    }

    /**
     * Checks if the game can remove a player.
     *
     * @return true if the game can remove a player.
     */
    public boolean canRemovePlayer() {
        return !players.isEmpty();
    }

    /*
     * Checks if the player can play.
     *
     * @param ID the ID of the player.
     * @return true if the player can play.
     */
    public boolean canPlay(String ID){
        return players.size() == PLAYER_COUNT && getPlayerIndex(ID) == playerIndexToPlay;
    }

    /**
     * Checks if the player move is valid.
     *
     * @param column the column where the player wants to play.
     * @param player the player
     * @return true if the move is valid.
     */
    public boolean canPlace(int column, String player){

        if(column >= GRID_WIDTH + GRID_START_INDEX || column < GRID_START_INDEX)
            return false;

        if(!canPlay(player))
            return false;

        return gameGrid[column - GRID_START_INDEX][GRID_HEIGHT - 1] == EMPTY_SQUARE_INDEX;
    }

    /**
     * Indicates if the game is waiting for player to join
     *
     * @return a boolean that indicates if the game is waiting for player to join
     */
    public boolean waitingForPlayers() {
        return players.size() != PLAYER_COUNT;
    }

    /**
     * Checks if the game is win.
     *
     * @return the ID of the winner or null if there's no winner
     */
    public String getGameWinner(){
        boolean isGameWin = false;
        for(int i = gameGrid.length-1; i >= 0; --i){
            for(int j = gameGrid[i].length-1; j >= 0; --j){
                if(gameGrid[i][j] == EMPTY_SQUARE_INDEX) continue;
                if(j > WIN_ROW_SIZE / 2){
                    isGameWin = horizontalCheck(i,j);
                }
                if(i > WIN_ROW_SIZE / 2){
                    isGameWin = verticalCheck(i, j);
                }
                if(i > WIN_ROW_SIZE / 2 && j < GRID_HEIGHT - WIN_ROW_SIZE / 2){
                    isGameWin = diagonalRightCheck(i, j);
                }
                if(j >= WIN_ROW_SIZE / 2 && i >= GRID_WIDTH - WIN_ROW_SIZE / 2){
                    isGameWin = diagonalLeftCheck(i, j);
                }

                if(isGameWin) return players.get(gameGrid[i][j]).ID;
            }
        }

        return null;
    }

    /**
     * Retrieve the player with the given ID
     *
     * @param ID of the player
     * @return Corresponding player
     */
    public PlayerState getPlayerWithID(String ID) {
        int index = getPlayerIndex(ID);
        if(index == -1) return null;
        else return players.get(index);
    }

    /**
     * Retrieve the player index in the players collection
     *
     * @param ID of the player
     * @return index of the player in the players collection
     */
    public int getPlayerIndex(String ID) {
        if(ID == null) return -1;

        int size = players.size();
        for (int i = 0; i < size; ++i)
            if(players.get(i).ID.equals(ID)) return i;

        return -1;
    }

    /**
     * Checks if there's four in a row horizontally.
     *
     * @param x the position in the x-axis.
     * @param y the position in the y-axis.
     * @return true if there's a 4 in a row.
     */
    private boolean horizontalCheck(int x, int y){
        return gameGrid[x][y] == gameGrid[x][y - 1] && gameGrid[x][y] == gameGrid[x][y - 2] &&
                gameGrid[x][y] == gameGrid[x][y - 3] ;
    }

    /**
     * Checks if there's four in a row vertically.
     *
     * @param x the position in the x-axis.
     * @param y the position in the y-axis.
     * @return true if there's a 4 in a row.
     */
    private boolean verticalCheck(int x, int y){
        return gameGrid[x][y] == gameGrid[x - 1][y] && gameGrid[x][y] == gameGrid[x - 2][y] &&
                gameGrid[x][y] == gameGrid[x - 3][y];
    }

    /**
     * Checks if there's four in a row diagonally.
     *
     * @param x the position in the x-axis.
     * @param y the position in the y-axis.
     * @return true if there's a 4 in a row.
     */
    private boolean diagonalRightCheck(int x, int y){
        return gameGrid[x][y] == gameGrid[x-1][y + 1] && gameGrid[x][y] == gameGrid[x-2][y + 2] &&
                gameGrid[x][y] == gameGrid[x-3][y + 3];
    }

    /**
     * Checks if there's four in a row diagonally.
     *
     * @param x the position in the x-axis.
     * @param y the position in the y-axis.
     * @return true if there's a 4 in a row.
     */
    private boolean diagonalLeftCheck(int x, int y){
        return gameGrid[x][y] == gameGrid[x-1][y - 1] && gameGrid[x][y] == gameGrid[x-2][y - 2] &&
                gameGrid[x][y] == gameGrid[x-3][y - 3];
    }

    /**
     * Get the grid of the game.
     *
     * @return a 2D array of char representing the grid.
     */
    public int[][] getGameGrid(){
        return this.gameGrid;
    }

    /**
     * Restart the game
     */
    public void restartGame(){
        gameGrid = new int[GRID_WIDTH][GRID_HEIGHT];

        for(int x = 0; x < GRID_WIDTH; ++x)
            for(int y = 0; y < GRID_HEIGHT; ++y)
                gameGrid[x][y] = EMPTY_SQUARE_INDEX;

        playerIndexToPlay = 0;
    }
}