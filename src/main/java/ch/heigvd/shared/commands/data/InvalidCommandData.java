package ch.heigvd.shared.commands.data;

import java.io.Serializable;

public class InvalidCommandData implements Serializable {

    private String message;

    public InvalidCommandData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
