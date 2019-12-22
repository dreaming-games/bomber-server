package server.handlers;

import server.clients.ClientSocket;

public interface ClientHandler {
    void onMessage(ClientSocket socket, String msg);
}
