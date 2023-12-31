package ch.heigvd.server.net;

import ch.heigvd.server.commands.ServerCommandsHandler;
import ch.heigvd.shared.abstractions.VirtualClient;
import ch.heigvd.shared.commands.CommandFactory;
import ch.heigvd.shared.logs.LogLevel;
import ch.heigvd.shared.logs.Logger;
import ch.heigvd.shared.commands.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable, VirtualClient {

    private final Socket socket;
    private final ServerCommandsHandler commandHandler = new ServerCommandsHandler(this);
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private String clientID;
    private boolean running = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        Logger.log(String.format("Connexion opened with %s", socket.getInetAddress()), this, LogLevel.Information);

        try (
            socket;
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            this.out = out;
            this.in = in;

            while (running) {
                try {
                    // Waiting for input message
                    Command inputCommand = (Command)in.readObject();
                    Logger.log(String.format("Command received : %s", inputCommand.type), this, LogLevel.Information);

                    // Handle the input command
                    commandHandler.handle(inputCommand);
                }
                catch (ClassNotFoundException ex) {
                    // Log error, notify and disconnect the client as we cannot continue
                    Logger.log(String.format("Error while deserializing object : %s", ex.getMessage()), this, LogLevel.Error);
                    sendCommand(CommandFactory.InvalidCommand("Server was unable to deserialize the input data"));
                    handleException();
                }
                catch (Exception ex) {
                    // Log error and disconnect the client as we cannot continue
                    Logger.log(String.format("Unhandled exception : %s", ex.getMessage()), this, LogLevel.Error);
                    handleException();
                }
            }
        }
        catch (IOException ex) {
            // Log error and disconnect the client as we cannot continue
            Logger.log(String.format("A socket error occurred : %s", ex.getMessage()), this, LogLevel.Error);
            handleException();
        }
        catch (Exception ex) {
            // Log error and disconnect the client as we cannot continue
            Logger.log(String.format("An unexpected error occurred : %s", ex.getMessage()), this, LogLevel.Error);
            handleException();
        }
        finally {
            Logger.log(String.format("Connexion closed with : %s", socket.getInetAddress()), this, LogLevel.Information);
        }
    }

    private void handleException() {
        commandHandler.handle(CommandFactory.QuitCommand());
        running = false;
    }

    @Override
    public boolean sendCommand(Command command) {
        try {
            out.writeObject(command);
            out.flush();
            out.reset();
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
        return clientID;
    }

    @Override
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
}
