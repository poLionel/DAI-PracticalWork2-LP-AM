package ch.heigvd.shared.game;

import java.io.Serializable;

public class PlayerState implements Serializable {
    public String ID;
    public char pawn;

    public PlayerState(String ID) {
        this.ID = ID;
    }

    public PlayerState(String ID, char pawn){this.ID = ID; this.pawn = pawn;};

}
