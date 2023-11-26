package ch.heigvd.client.ui;

import ch.heigvd.shared.game.GameState;
import ch.heigvd.shared.game.PlayerState;

import java.util.Scanner;

public class GameUI {
    private static GameUI instance = null;

    private GameUI() {
    }

    public static synchronized GameUI getInstance() {
        if (instance == null)
            instance = new GameUI();

        return instance;
    }

    public void displayGameState(GameState gameState) {
        PlayerState[] players = gameState.getPlayers();
        for (PlayerState player : players) {
            System.out.println(player.ID);
        }
    }

    public int getPosition(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez la position où vous voulez jouer : ");
        boolean correctInput = false;
        int input = -1;
        while(!correctInput){
            try{
                input = Integer.parseInt(scanner.nextLine());
                correctInput = true;
            }catch(NumberFormatException e) {
                System.out.println("Saisie incorrect... Veuillez réessayer");
            }
        }

        return input;
    }
}
