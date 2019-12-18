package server.manager;

import game.field.FieldParser;
import game.field.GameField;
import game.play.Game;
import game.play.Player;

import server.clients.ClientSocket;

import java.util.List;
import java.util.ArrayList;

public class GameRunner {
    private static GameRunner singleton;
    public static GameRunner getGameRunner() {
        if (singleton == null) {
            singleton = new GameRunner();
        }
        return singleton;
    }


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



    /////////////////////////////////////////////
    ///  Functions to get status for logging  ///
    /////////////////////////////////////////////

    public int runningGames() {
        return runningGames.size();
    }

    public boolean joiningGame() {
        return (currentGame != null && currentGame.players() > 0);
    }
}
