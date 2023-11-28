package ch.heigvd.shared.game;

import java.io.Serializable;

public class PlayerState implements Serializable {
    public String ID;

    public PlayerState(String ID) {
        this.ID = ID;
    }
}
