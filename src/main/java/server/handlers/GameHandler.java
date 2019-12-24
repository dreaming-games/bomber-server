package server.handlers;

import game.field.FieldParser;
import game.field.GameField;
import game.play.Game;
import game.play.Player;
import server.clients.ClientSocket;

abstract class GameHandler implements ClientHandler {
    final ClientSocket[] clients;
    final Game game;

    /////////////////////////////////////////////////////////////////
    ////// Constructors for either a new game or passing along //////
    /////////////////////////////////////////////////////////////////

    GameHandler(String mapFile) {
        GameField field = new FieldParser().fromBMapFile(mapFile);
        this.game = new Game(field);
        // Every player ID will also be the slot in this socket array
        this.clients = new ClientSocket[game.playerSlots()];
    }

    GameHandler(ClientSocket[] sockets, Game game) {
        this.clients = sockets;
        this.game = game;
    }

    //////////////////////////////////////////////////////
    ////// Managing the game / player clients in it //////
    //////////////////////////////////////////////////////

    void addToGame(Player p, ClientSocket socket) {
        this.game.addPlayer(p);
        socket.inGameId = p.getId();
        this.clients[p.getId()] = socket;
    }

    void removeFromGame(int id) {
        this.game.removePlayer(this.game.getPlayers()[id]);
        this.clients[id].inGameId = -1;
        this.clients[id] = null;
    }

    ///////////////////////////////////////////////////
    ////// Some help with handling communication //////
    ///////////////////////////////////////////////////

    void send(ClientSocket client, String msg) {
        if (client == null || client.send(msg)) {
            return;
        }
        // Failed to send
        System.err.println("Removing player " + client.inGameId + " due to fail in sending");
        removeFromGame(client.inGameId);
    }

    void broadcast(String msg) {
        for (ClientSocket client : this.clients) {
            send(client, msg);
        }
    }
}
