package server.clients;

import server.handlers.ClientHandler;
import server.main.EasyLogger;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSocket {
    private static final Logger LOGGER = EasyLogger.getLogger("Client");

    private final int clientID;
    private final Socket connection;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    public ClientHandler handler;
    public int inGameId;

    private void messageLoop() {
        // Keep receiving messages and sent them to your handler
        while (!connection.isClosed()) {
            try {
                String in = reader.readLine();
                if (in == null) break;
                this.handler.onMessage(this, in);
            } catch (IOException e) {
                break;
            }
        }
        LOGGER.log(Level.INFO, "Connection with client " + clientID + " closed");
    }

    public ClientSocket(int id, Socket connection, ClientHandler handler) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        this.connection = connection;
        this.handler = handler;
        this.clientID = id;
        // Start listening thread
        Thread thread = new Thread(this::messageLoop);
        thread.setDaemon(true);
        thread.start();
    }

    public boolean send(String message) {
        try {
            this.writer.write(message + "\n");
            this.writer.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
