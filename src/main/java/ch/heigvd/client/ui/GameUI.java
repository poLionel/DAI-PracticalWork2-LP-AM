package ch.heigvd.client.ui;

import ch.heigvd.shared.game.GameState;
import ch.heigvd.shared.game.PlayerState;

import java.util.Scanner;

/**
 * User interface of a game.
 */
public class GameUI {
    /**
     * todo
     */
    private static GameUI instance = null;

    /**
     * A scanner used to get the input of the client.
     */
    private final Scanner scanner = new Scanner(System.in);

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
    public void displayGameState(GameState gameState) {
        PlayerState[] players = gameState.getPlayers();
        for (PlayerState player : players) {
            System.out.println(player.ID);
        }

        if(gameState.isGameWin()){
            System.out.println("The game is over. The winner is " + gameState.playerWhoPlayed);
            System.out.println("The game is starting again...");
        }

        char[][] gameGrid = gameState.getGameGrid();
        System.out.println("-------------------> " + gameGrid[5][3]);
        if(gameGrid != null){
            for(char[] row : gameGrid){
                for(char column : row){
                    System.out.print("|" + column + "|");
                }
                System.out.println();
            }
        }
    }

    /**
     * Getter for the user input.
     *
     * @return the user input.
     */
    public int getInput(){

        System.out.println("Enter the position you want to play [1-7] or enter 'FF' to quit ");
        boolean correctInput = false;
        int input = -1;
        while(!correctInput){
            try{
                input = Integer.parseInt(scanner.nextLine());
                correctInput = true;
            }catch(NumberFormatException e) {
                System.out.println("The position you entered is not valid. Please try again");
            }
        }

        return input;
    }
}
