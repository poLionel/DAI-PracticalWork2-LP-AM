package ch.heigvd.client.ui;

import ch.heigvd.shared.game.GameState;

import java.util.Scanner;

/**
 * User interface of a game.
 */
public class GameUI {

    private static GameUI instance = null;

    /**
     * A scanner used to get the input of the client.
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * A char array that correspond to the pawn display character
     */
    private static final char[] PAWNS =  {' ', 'X', 'O' };

    /**
     * Indicates if the game was previously started
     */
    private boolean wasStarted = false;

    /**
     * Constructor
     */
    private GameUI() {
    }

    /**
     * Getter for an instance of GameUI.
     *
     * @return an instance of GameUI.
     */
    public static synchronized GameUI getInstance() {
        if (instance == null)
            instance = new GameUI();

        return instance;
    }

    /**
     * Display of the game.
     *
     * @param gameState the game to be displayed.
     */
    public void displayGameState(GameState gameState, String clientID) {


        if(gameState.waitingForPlayers()) {
            if(wasStarted)
                displayDisconnectedMessage();

            displayWaitingPlayerMessage();
            wasStarted = false;
            return;
        }

        wasStarted = true;

        displayGrid(gameState.getGameGrid());

        String winnerID = gameState.getGameWinner();
        if(winnerID != null) {
            if(winnerID.equals(clientID)) displayWinMessage();
            else displayLooseMessage();
            displayRestartMessage();
            return;
        }

        if(gameState.canPlay(clientID)) {
            displayYourTurnMessage();
        }
        else {
            displayNotTurnMessage();
        }
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayWinMessage() {
        System.out.println("You won this game !");
    }

    public void displayLooseMessage() {
        System.out.println("You lost this game...");
    }

    public void displayYourTurnMessage() {
        System.out.println("It's your turn to play !");
    }

    public void displayNotTurnMessage() {
        System.out.println("It's the other player turn");
    }

    public void displayRestartMessage() {
        System.out.println("The game will soon restart");
    }

    public void displayWaitingPlayerMessage() {
        System.out.println("Waiting for other player to join");
    }

    public void displayDisconnectedMessage() {
        System.out.println("A player has disconnected");
        displayRestartMessage();
    }

    /**
     * Getter for the user input.
     *
     * @return the user input.
     */
    public int getInput(GameState gameState, String clientID){

        System.out.printf("Enter the position you want to play [%s-%s] or enter 'FF' to quit ", GameState.GRID_START_INDEX, GameState.GRID_START_INDEX + GameState.GRID_WIDTH - 1);
        System.out.println();
        boolean correctInput = false;
        int input = -1;
        while(!correctInput){
            try{
                input = Integer.parseInt(scanner.nextLine());
                if(!gameState.canPlace(input, clientID)) {
                    System.out.println("You cannot place at this position");
                }
                else {
                    correctInput = true;
                }
            }catch(NumberFormatException e) {
                System.out.println("The position you entered is not valid. Please try again");
            }
        }

        return input;
    }

    private void displayGrid(int[][] grid) {
        if(grid != null){

            // Display grid content
            for(int y =  GameState.GRID_HEIGHT - 1; y >= 0; --y) {
                for (int x = 0; x < GameState.GRID_WIDTH; ++x) {
                    System.out.print("|" + PAWNS[grid[x][y] - GameState.EMPTY_SQUARE_INDEX]);
                }
                System.out.println("|");
            }

            // Display column indexes
            for(int x = 0; x < GameState.GRID_WIDTH; ++x) {
                System.out.printf(" %d", x + GameState.GRID_START_INDEX);
            }

            System.out.println();
        }
    }
}
