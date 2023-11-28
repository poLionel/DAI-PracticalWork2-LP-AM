package ch.heigvd.shared.game;

import java.io.Serializable;

public class PlayerState implements Serializable {
    public String ID;
    public static int nbPlayer = 0;
    public int playerNb;

    public PlayerState(String ID) {
        this.ID = ID;
        playerNb = nbPlayer;
        ++nbPlayer;
    }

}
