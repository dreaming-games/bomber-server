package server;

import java.net.Socket;

public class ClientSocket {
    private final Socket connection;

    ClientSocket(Socket connection) {
        this.connection = connection;
    }
}
