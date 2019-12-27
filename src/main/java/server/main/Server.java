package server.main;

import server.clients.ClientSocket;
import server.handlers.ClientHandler;
import server.handlers.JoinHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = EasyLogger.getLogger("Server");

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        System.out.println("\n");
        ClientHandler handler = new JoinHandler();

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
        int currentClientId = 0;
        while (true) {
            try {
                new ClientSocket(++currentClientId, serverSocket.accept(), handler);
                LOGGER.log(Level.INFO,"Accepted a new client socket");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to accept socket:" + e.getMessage());
            }
        }
    }
}
