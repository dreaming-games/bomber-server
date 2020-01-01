package server.handlers;

import bomber.field.GameField;
import bomber.entity.Player;
import server.clients.ClientSocket;

public class JoinHandler implements ClientHandler {
    private GameHandle gameHandle;

    public JoinHandler() {
        this.gameHandle = null;
    }

    private void join(ClientSocket client, String name) {
        // Find a spawn point for this player and add player to this game
        int playerId = this.gameHandle.game.nextFreePlayer();
        Player clientPlayer = new Player(playerId, name,
                this.gameHandle.game.spawnPoint(playerId));
        this.gameHandle.addToGame(clientPlayer, client);
        ClientHandler oldHandler = client.handler;
        client.handler = this;
        if (!client.send("joined " + clientPlayer.getId())) {
            this.gameHandle.removeFromGame(playerId);
            client.handler = oldHandler;
            update();
        }
        update();
    }

    private String playerNames() {
        StringBuilder sb = new StringBuilder();
        for (Player p : this.gameHandle.game.getPlayers()) {
            if (p != null) sb.append("named ").append(p.getId())
                    .append(" ").append(p.getName()).append("\n");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    private synchronized void update() {
        String joining = "joining " + this.gameHandle.game.getJoinedPlayers() +
                "/" + this.gameHandle.game.playerSlots();
        this.gameHandle.broadcast(joining);

        // Check if we are ready to play!
        if (this.gameHandle.game.isFull()) {
            this.gameHandle.broadcast(playerNames());

            GameField field = this.gameHandle.game.getField();
            this.gameHandle.broadcast("loading " + field.getWidth() + "/" + field.getHeight());
            for (int i = 0; i < field.getHeight(); i++) {
                this.gameHandle.broadcast("map " + i + " " + field.toString(i));
            }

            new PlayHandler(this.gameHandle);
        }
    }

    @Override
    public synchronized void onMessage(ClientSocket socket, String msg) {
        String[] msgParts = msg.split(" ", 2);
        switch (msgParts[0]) {
            case "join":
                if (this.gameHandle == null || this.gameHandle.game.isFull()
                        || this.gameHandle.game.gameTick().getTickNum() > 0) {
                    this.gameHandle = new GameHandle("practice.bmap");
                }
                this.join(socket, msgParts[1]);
                break;
            case "practice":
                // Todo
                break;
            case "update":
                update();
                break;
            case "names":
                socket.send(playerNames());
                break;
        }
    }
}
