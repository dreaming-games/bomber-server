package server.handlers;

import bomber.field.GameField;
import bomber.entity.Player;
import server.clients.ClientSocket;

import java.util.ArrayList;

public class JoinHandler implements ClientHandler {
    private final ArrayList<PlayHandler> playHandlers;
    private GameHandle joiningGameHandle;

    public JoinHandler() {
        playHandlers = new ArrayList<>();
    }

    @Override
    public synchronized void onMessage(ClientSocket socket, String msg) {
        // Don't have a game to join people to? Make a new game handle
        if (this.joiningGameHandle == null || this.joiningGameHandle.game.isFull()
                || this.joiningGameHandle.game.gameTick().getTickNum() > 0) {
            this.joiningGameHandle = new GameHandle("1v1.bmap");
        }
        String[] msgParts = msg.split(" ", 2);
        switch (msgParts[0]) {
            case "join":
                join(socket, msgParts[1]);
                break;
            case "games":
                socket.send("games " + gameIds());
            case "spectate":
                // Todo
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

    private void join(ClientSocket client, String name) {
        // Find a spawn point for this player and add player to this game
        int playerId = this.joiningGameHandle.game.nextFreePlayer();
        Player clientPlayer = new Player(playerId, name,
                this.joiningGameHandle.game.spawnPoint(playerId));
        this.joiningGameHandle.addToGame(clientPlayer, client);

        ClientHandler oldHandler = client.handler;
        client.handler = this;
        if (!client.send("joined " + clientPlayer.getId())) {
            this.joiningGameHandle.removeFromGame(playerId);
            client.handler = oldHandler;
            update();
        }
        update();
    }

    private void update() {
        String joining = "joining " + this.joiningGameHandle.game.getJoinedPlayers() +
                "/" + this.joiningGameHandle.game.playerSlots();
        this.joiningGameHandle.broadcast(joining);

        // Check if we are ready to play!
        if (this.joiningGameHandle.game.isFull()) {
            this.joiningGameHandle.broadcast(playerNames());

            GameField field = this.joiningGameHandle.game.getField();
            this.joiningGameHandle.broadcast("loading " + field.getWidth() + "/" + field.getHeight());
            for (int i = 0; i < field.getHeight(); i++) {
                this.joiningGameHandle.broadcast("map " + i + " " + field.toString(i));
            }

            new PlayHandler(this.joiningGameHandle, this::onGameDone);
        }
    }

    ///////////////////////////////////////////////
    ////// Keeping track of the playHandlers //////
    ///////////////////////////////////////////////

    private synchronized void onGameDone(PlayHandler p) {
        // Remove from playHandlers
        playHandlers.remove(p);
    }

    ///////////////////////////////////////////
    ////// Some nice string making stuff //////
    ///////////////////////////////////////////

    private String gameIds() {
        StringBuilder sb = new StringBuilder();
        for (PlayHandler playHandler : playHandlers) {
            sb.append(playHandler.gameHandle.handleId).append(" ");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    private String joiningString() {
        return joiningGameHandle.game.getJoinedPlayers() + "/"
                + joiningGameHandle.game.getPlayers().length;
    }

    public synchronized String stateString() {
        // [X + A/B] X is games playing, A out of B people joined for next game
        return "[" + playHandlers.size() + " + " + joiningString() + "]";
    }

    private String playerNames() {
        StringBuilder sb = new StringBuilder();
        for (Player p : this.joiningGameHandle.game.getPlayers()) {
            if (p != null) sb.append("named ").append(p.getId())
                    .append(" ").append(p.getName()).append("\n");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
