package server;

import game.field.FieldParser;
import game.field.GameField;
import game.play.Game;
import game.play.Player;
import game.general.Point;

import server.clients.ClientSocket;

import java.util.List;
import java.util.ArrayList;


public class GameRunner {
    private Game currentGame;
    private final List<Game> runningGames;

    public GameRunner() {
        this.runningGames = new ArrayList<>();
        loadNewGame();
    }

    private void loadNewGame() {
        // Load the specified map for this game
        GameField field = new FieldParser().fromBMapFile("./classic.bmap");
        System.out.println("Loaded new map: \n" + field.toString());
        this.currentGame = new Game(field);
    }

    public void addToGame(ClientSocket client, String name) {
        if (currentGame == null || currentGame.isFull()) {
            loadNewGame();
        }
        // Find a spawnpoint for this player and add player to this game
        Player clientPlayer = new Player(this.currentGame.nextSpawnPoint(), name);
        this.currentGame.addPlayer(clientPlayer);
    }
}
