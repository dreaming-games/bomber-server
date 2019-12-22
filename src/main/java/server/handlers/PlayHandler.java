package server.handlers;

import game.play.Game;
import server.clients.ClientSocket;

public class PlayHandler implements ClientHandler {
    private final ClientSocket[] clients;
    private final Game game;

    PlayHandler(Game game, ClientSocket[] clients) {
        this.game = game;
        this.clients = clients;
        for (ClientSocket client : clients) {
            client.setHandler(this);
        }
    }

    @Override
    public void onMessage(ClientSocket socket, String msg) {

    }
}
