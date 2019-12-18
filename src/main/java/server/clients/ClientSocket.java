package server.clients;

import java.net.Socket;

public class ClientSocket {
    private final Socket connection;
    private Thread thread;

    public ClientSocket(Socket connection, ClientHandler handler) {
        this.connection = connection;
        this.thread = new Thread(() -> {

        });
        this.thread.start();
    }
}
