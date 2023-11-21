package ch.heigvd.server.net;

import ch.heigvd.server.commands.ServerCommandsHandler;
import ch.heigvd.shared.logs.LogLevel;
import ch.heigvd.shared.logs.Logger;
import ch.heigvd.shared.commands.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Logger logger = Logger.getInstance();
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            socket;
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {

            Command inputCommand = null;
            Command outputCommand = null;

            while (true) {

                try {

                    // Waiting for input message
                    inputCommand = (Command)in.readObject();
                    logger.log(String.format("Input recieved : %s", inputCommand), this, LogLevel.Information);

                    // Calculate and send the output message
                    outputCommand = ServerCommandsHandler.handle(inputCommand);
                    if(outputCommand != null) out.writeObject(outputCommand);
                    logger.log(String.format("Output sent : %s", outputCommand), this, LogLevel.Information);
                }
                catch (ClassNotFoundException ex) {
                    logger.log(String.format("Error while deserializing object : %s", ex.getMessage()), this, LogLevel.Error);
                }
                catch (Exception ex) {
                    logger.log(String.format("Unhandled exception : %s", ex.getMessage()), this, LogLevel.Error);
                }
            }

        } catch (IOException ex) {
            logger.log(String.format("A socket error occured : %s", ex.getMessage()), this, LogLevel.Error);
        }
    }
}
