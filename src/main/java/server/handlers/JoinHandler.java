package server.handlers;

import game.field.GameField;
import game.play.Player;
import server.clients.ClientSocket;

public class JoinHandler extends GameHandler {
    JoinHandler() {
        super("practice.bmap");
    }

    void join(ClientSocket client, String name) {
        // Find a spawn point for this player and add player to this game
        int playerId = this.game.nextFreePlayer();
        Player clientPlayer = new Player(playerId, name,
                this.game.spawnPoint(playerId));
        addToGame(clientPlayer, client);
        ClientHandler oldHandler = client.handler;
        client.handler = this;
        if (client.send("joined " + clientPlayer.getId())) {
            update();
        } else {
            removeFromGame(playerId);
            client.handler = oldHandler;
        }
    }

    private String playerNames() {
        StringBuilder sb = new StringBuilder();
        for (Player p : this.game.getPlayers()) {
            if (p != null) sb.append("named ").append(p.getId())
                    .append(" ").append(p.getName()).append("\n");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    private void update() {
        // Update all clients
        String joining = "joining " + this.game.getJoinedPlayers() +
                "/" + this.game.playerSlots();
        broadcast(joining);
        // Check if we are ready to play!
        if (this.game.isFull()) {
            GameField field = this.game.getField();
            // Send all player names and 'loading' message
            broadcast(playerNames());
            broadcast("loading " + field.getWidth() + "/" + field.getHeight());
            // Send the map to be played and let 'PlayHandler' take over
            for (int i = 0; i < field.getHeight(); i++) {
                broadcast("map " + i + " " + field.toString(i));
            }
            new PlayHandler(game, clients);
        }
    }

    boolean isFull() {
        return this.game.isFull();
    }

    @Override
    public void onMessage(ClientSocket socket, String msg) {
        switch (msg) {
            case "update":
                update();
                break;
            case "names":
                socket.send(playerNames());
                break;
        }
    }
}
