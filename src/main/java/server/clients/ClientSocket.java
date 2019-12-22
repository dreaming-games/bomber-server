package server.clients;

import lombok.Setter;
import server.handlers.ClientHandler;

import java.io.*;
import java.net.Socket;

public class ClientSocket {
    private final int clientID;
    private final Socket connection;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    @Setter
    private ClientHandler handler;

    private void messageLoop() {
        // Keep receiving messages and sent them to your handler
        while (!connection.isClosed()) {
            try {
                String in = reader.readLine();
                if (in == null) break;
                this.handler.onMessage(this, in);
            } catch (IOException e) {
                System.err.println("Failed to read line on socket from client " + clientID + ": " + e.getMessage());
            }
        }
        System.err.println("Connection with client " + clientID + " closed");
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
            e.printStackTrace();
            return false;
        }
    }
}
