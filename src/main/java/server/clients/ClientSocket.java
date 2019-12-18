package server.clients;

import java.net.Socket;

public class ClientSocket {
    private final Socket connection;
    private final ClientHandler handler;
    private Thread thread;

    private void getMessage() {

    }

    public ClientSocket(Socket connection, ClientHandler handler) {
        this.handler = handler;
        this.connection = connection;
        this.thread = new Thread(() -> {
            while (true) {
                getMessage();
            }
        });
        this.thread.start();
    }
}
