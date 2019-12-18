package server.clients;

import server.manager.GameRunner;

import java.util.List;
import java.util.ArrayList;

public class ClientHandler {
    private final List<ClientSocket> clients;
    private final GameRunner gameRunner;

    public ClientHandler(GameRunner gameRunner) {
        this.clients = new ArrayList<>();
        this.gameRunner = gameRunner;
    }

    public void add(ClientSocket client) {

    }

}
