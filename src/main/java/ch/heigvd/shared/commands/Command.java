package ch.heigvd.shared.commands;

import java.io.Serializable;

public class Command implements Serializable {
    public CommandType type;
    public Object value;
}
