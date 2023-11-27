package ch.heigvd.shared.game;

import ch.heigvd.shared.abstractions.VirtualClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the rules and the logics of the game.
 */
public class GameState implements Serializable {

    /** The maximum player in the game. */
    public static final int MAX_PLAYER_COUNT = 2;

    /** A list of the player. */
    private final List<PlayerState> players = Collections.synchronizedList(new ArrayList<>());

    /** The grid of the game. */
    private char[][] gameGrid = new char[6][7];

    /** the player that has to play.*/
    public String playerWhoPlayed;

    private int turn = 0;

    /**The display of the token for the player 1 */
    private final char player_1 = 'X';

    /** The display of the token for the player 2 */
    private final char player_2 = 'O';

    /** THe display of an empty case. */
    private final char emptyCase = '\u0000';

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
            playerWhoPlayed = ID;
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

    /**
     * Gets the players connected to the game.
     *
     * @return an array of the players.
     */
    public PlayerState[] getPlayers() {
        PlayerState[] copy = new PlayerState[players.size()];
        players.toArray(copy);
        return copy;
    }

    /**
     * Checks if the game can add a player.
     *
     * @return true if the game can add a player.
     */
    public boolean canAddPlayer() {
        return players.size() < MAX_PLAYER_COUNT;
    }

    /**
     * Checks if the game can remove a player.
     *
     * @return true if the game can remove a player.
     */
    public boolean canRemovePlayer() {
        return !players.isEmpty();
    }

    /**
     * todo
     *
     * @param playerID
     * @return
     */
    public boolean containsPlayer(String playerID) {
        return getPlayerWithID(playerID) != null;
    }

    /**
     * todo
     *
     * @param ID
     * @return
     */
    private synchronized PlayerState getPlayerWithID(String ID) {
        if(ID == null) return null;
        for (PlayerState player : players)
            if(player.ID.equals(ID)) return player;
        return null;
    }

    /**
     * Checks if the player can play.
     *
     * @param ID the ID of the player.
     * @return true if the player can play.
     */
    public synchronized boolean canPlay(String ID){
        return ID != null && players.size() == 2 && !ID.equals(playerWhoPlayed);
    }

    /**
     * Checks if the player move is valid.
     *
     * @param column the column where the player wants to play.
     * @param player the player
     * @return true if the move is valid.
     */
    public synchronized boolean validPosition(int column, String player){
        boolean isValidMove = false;

        if(column >= 7 || column < 0)
            return isValidMove;

        for(int i = 5; i >= 0; --i){
            if(gameGrid[i][column] == emptyCase){
                gameGrid[i][column] = turn % 2 == 0 ? 'X' : '0';
                isValidMove = true;
                playerWhoPlayed = player;
                i = 0;
                ++turn;
            }
        }

        return isValidMove;
    }

    public synchronized void place(GameState gameState){
        gameState.getGameGrid();

    }

    /**
     * Checks if the game is win.
     *
     * @return true if someone win the game.
     */
    public boolean isGameWin(){
        boolean isGameWin = false;
        for(int i = gameGrid.length-1; i >= 0; --i){
            for(int j = gameGrid[i].length-1; j >= 0; --j){
                if(j > 2 && !isGameWin){
                    isGameWin = horizontalCheck(i,j);
                }
                if(i > 2 && !isGameWin){
                    isGameWin = verticalCheck(i, j);
                }
                if(i > 2 && j < 4 && !isGameWin){
                    isGameWin = diagonalRightCheck(i, j);
                }
                if(j >= 3 && i >= 3 && !isGameWin){
                    isGameWin = diagonalLeftCheck(i, j);
                }
            }
        }

        return isGameWin;
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
                gameGrid[x][y] == gameGrid[x][y - 3];
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
    public char[][] getGameGrid(){
        return this.gameGrid;
    }

    /**
     * Reset the grid.
     */
    public void resetGameGrid(){
        gameGrid = new char[6][7];
    }

    /**
     * Todo delete that
     */
    public void draw(){
        if(gameGrid != null){
            for(char[] row : gameGrid){
                for(char column : row){
                    System.out.print("|" + column + "|");
                }
                System.out.println();
            }
        }
    }
}