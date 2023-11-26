package ch.heigvd.client.net;

import ch.heigvd.client.commands.ClientCommandsHandler;
import ch.heigvd.shared.abstractions.VirtualClient;
import ch.heigvd.shared.abstractions.VirtualEndpoint;
import ch.heigvd.shared.commands.Command;
import ch.heigvd.shared.logs.LogLevel;
import ch.heigvd.shared.logs.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameClient implements VirtualClient {
    public static final int DEFAULT_PORT = 5187;
    private final int port;
    private final String address;
    private boolean askedForClosing;
    private final ClientCommandsHandler commandHandler = new ClientCommandsHandler(this);
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String clientID;

    public GameClient(String address) {
        this(address, DEFAULT_PORT);
    }
    public GameClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {

        Logger.log(String.format("Connexion to %s on port %s", address, port), this, LogLevel.Information);

        try(
                Socket clientSocket = new Socket(address, port);
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

        ) {
            this.out = out;
            this.in = in;

            Logger.log("Connected successfully", this, LogLevel.Information);

            // Send the initial command
            commandHandler.sendInitialCommand();

            while (!askedForClosing) {
                try {

                    // Waiting for input message
                    Command inputCommand = (Command) in.readObject();
                    Logger.log(String.format("Command recieved : %s", inputCommand.type), this, LogLevel.Information);

                    // Handle the input command
                    commandHandler.handle(inputCommand);
                }
                catch (ClassNotFoundException ex) {
                    // Log error
                    Logger.log(String.format("Error while deserializing object : %s", ex.getMessage()), this, LogLevel.Error);
                }
                catch (Exception ex) {
                    // Log error
                    Logger.log(String.format("Unhandled exception : %s", ex.getMessage()), this, LogLevel.Error);
                }
            }
        }
        catch (IOException ex) {
            Logger.log(String.format("Handled error : %s", ex.getMessage()), this, LogLevel.Error);
        }
        catch (Exception ex) {
            Logger.log(String.format("An unexpected error occurred : %s", ex.getMessage()), this, LogLevel.Error);
        }
        finally {
            askedForClosing = false;
        }
    }

    public void shutdown() {
        askedForClosing = true;
    }

    @Override
    public boolean sendCommand(Command command) {
        try {
            out.writeObject(command);
            Logger.log(String.format("Command sent : %s", command.type), this, LogLevel.Information);
            return true;
        }
        catch (IOException ex) {
            Logger.log(String.format("Unable to send command : %s", ex.getMessage()), this, LogLevel.Error);
            return false;
        }
    }

    @Override
    public String getClientID() {
        return this.clientID;
    }

    @Override
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
}
