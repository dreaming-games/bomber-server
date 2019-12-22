package server.main;

import server.clients.ClientSocket;
import server.handlers.IdleHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        System.out.println("\n");
        IdleHandler idleHandler = new IdleHandler();
        Logger LOGGER = EasyLogger.getLogger("Server");

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
                new ClientSocket(++currentClientId, serverSocket.accept(), idleHandler);
                LOGGER.log(Level.INFO,"Accepted a new client socket");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to accept socket:" + e.getMessage());
            }
        }
    }
}
