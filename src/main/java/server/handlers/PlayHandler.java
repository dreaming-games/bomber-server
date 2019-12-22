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
            client.handler = this;
        }
    }

    @Override
    public void onMessage(ClientSocket socket, String msg) {
        String[] msgParts = msg.split(" ", 2);
        switch (msgParts[0]) {
            case "drop": // Drop a bomb

                break;
            case "move": // Move in a certain direction

                break;
        }
    }
}
