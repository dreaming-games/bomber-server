package server;

import game.field.FieldParser;
import game.field.GameField;
import game.play.Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = EasyLogger.getLogger(Server.class.getName());

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {



        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(6969);
            LOGGER.log(Level.INFO,"Started Server on port " + serverSocket.getLocalPort());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Error creating server socket:");
            LOGGER.log(Level.SEVERE,e.getMessage() + " -- Cause: " + e.getCause());
            return;
        }

        while (true) {
            // Load the specified map for this game
            GameField field = new FieldParser().fromBMapFile("./classic.bmap");
            Game currentGame = new Game(field);

            // Accept the right amount of playing users for that map
            while (!currentGame.isFull()) {
                try {
                    Socket newUser = serverSocket.accept();
                    LOGGER.log(Level.INFO,"Accepted a new user socket");
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error creating server socket:" + e.getMessage());
                }
            }

            // Actually play the game

        }
    }
}
