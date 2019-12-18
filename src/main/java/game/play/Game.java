package game.play;

import game.field.GameField;
import game.general.Point;

import java.util.ArrayList;

public class Game {
    private final GameField field;
    private final ArrayList<Player> players;
    private final ArrayList<Listener> listeners;


    public Game(GameField field) {
        this.field = field;
        this.players = new ArrayList<>(field.getSpawnPoints().length);
        this.listeners = new ArrayList<>(1);
    }

    public boolean isFull() {
        return this.players.size() == field.getSpawnPoints().length;
    }



    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public Point nextSpawnPoint() {
        return field.getSpawnPoints()[this.players.size()];
    }
}
