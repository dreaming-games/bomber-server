package game.play;

import game.field.GameField;

import java.util.ArrayList;

public class Game {
    private final GameField field;
    private final ArrayList<Player> players;

    public Game(GameField field) {
        this.field = field;
        this.players = new ArrayList<>(
                field.getSpawnPoints().length);
    }

    public boolean isFull() {
        return this.players.size() == field.getSpawnPoints().length;
    }

}
