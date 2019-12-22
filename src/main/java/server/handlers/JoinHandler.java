package server.handlers;

import game.field.FieldParser;
import game.field.GameField;
import game.play.Game;
import game.play.Player;
import server.clients.ClientSocket;

public class JoinHandler implements ClientHandler {
    private final ClientSocket[] clients;
    private final Game game;

    JoinHandler() {
        // Load the specified map for this game
        GameField field = new FieldParser().fromBMapFile("./classic.bmap");
        // System.out.println("Loaded new map for game: \n" + field.toString());
        this.game = new Game(field);
        // Every player ID will also be the slot in this socket array
        this.clients = new ClientSocket[game.playerSlots()];
    }

    void join(ClientSocket client, String name) {
        // Find a spawn point for this player and add player to this game
        int playerId = this.game.nextFreePlayer();
        Player clientPlayer = new Player(playerId, name,
                this.game.spawnPoint(playerId));
        this.game.addPlayer(clientPlayer);
        this.clients[clientPlayer.getId()] = client;
        client.setHandler(this);
        client.send("joined " + clientPlayer.getId());
        update();
    }

    private void sendNames(ClientSocket socket) {
        for (Player p : this.game.getPlayers()) {
            if (p != null) socket.send("named " +
                    p.getId() + " " + p.getName());
        }
    }

    private void update() {
        boolean sendFail = false;
        // Update all clients
        for (ClientSocket c : this.clients) {
            if (c != null) {
                sendFail |= c.send("joining " +
                        this.game.getJoinedPlayers() +
                        "/" + this.game.playerSlots());
            }
        }
        // Todo: if a send fails... I guess kick them from the game?
        // Check if we are ready to play!
        if (this.game.isFull()) {
            for (ClientSocket c : clients) {
                // Send all player names
                sendNames(c);
                // Send the map to be played
                c.send(this.game.getField().toString());
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
                sendNames(socket);
                break;
        }
    }
}
