package server.handlers;

import bomber.field.FieldParser;
import bomber.field.GameField;
import bomber.game.Game;
import bomber.game.Settings;
import bomber.entity.Player;
import server.clients.ClientSocket;

import java.io.IOException;

class GameHandle {
    // Load settings for all games to come
    private static final Settings gameSettings;
    static {
        gameSettings = new Settings();
        try {
            gameSettings.addSettingsFromFile("settings");
        } catch (IOException e) {
            System.err.println("Did not load settings file: " + e.getMessage());
        }
    }

    // Give this game handle a unique ID
    private static int maxHandleId = 0;
    final int handleId;
    // Keep track of the game and clients
    private final ClientSocket[] clients;
    final Game game;

    GameHandle(String mapFile) {
        this.handleId = ++maxHandleId;
        GameField field = new FieldParser().fromBMapFile(mapFile);
        this.game = new Game(field, gameSettings);
        // Every player ID will also be the slot in this socket array
        this.clients = new ClientSocket[game.playerSlots()];
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

    private void send(ClientSocket client, String msg) {
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

    void setClientHandler(ClientHandler handler) {
        for (ClientSocket client : this.clients) {
            if (client == null) continue;
            client.handler = handler;
        }
    }
}
