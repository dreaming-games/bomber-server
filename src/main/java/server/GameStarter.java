package server;

import game.field.FieldParser;
import game.field.GameField;
import game.play.Game;
import server.clients.ClientSocket;

public class GameStarter {
    private Game currentGame;

    private void loadNewGame() {
        // Load the specified map for this game
        GameField field = new FieldParser().fromBMapFile("./classic.bmap");
        this.currentGame = new Game(field);
    }

    public void addToGame(ClientSocket client) {
        if (currentGame == null || currentGame.isFull()) {
            loadNewGame();
        }
        // Todo: Add this client to the current game
    }
}
