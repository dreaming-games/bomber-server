package server;

import server.clients.ClientHandler;
import server.clients.ClientSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = EasyLogger.getLogger(Server.class.getName());

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        System.out.println("\n");

        // Start server socket
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(6969);
            LOGGER.log(Level.INFO,"Started Server on port " + serverSocket.getLocalPort());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Error creating server socket:");
            LOGGER.log(Level.SEVERE,e.getMessage() + " -- Cause: " + e.getCause());
            return;
        }

        // Accept all the clients you can get
        ClientHandler clientHandler = new ClientHandler();
        while (true) {
            try {
                Socket newUser = serverSocket.accept();
                clientHandler.add(new ClientSocket(newUser, clientHandler));
                LOGGER.log(Level.INFO,"Accepted a new user socket");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error creating server socket:" + e.getMessage());
            }
        }
    }
}
