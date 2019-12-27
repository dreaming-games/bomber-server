package bomber.game;

import bomber.field.GameField;
import bomber.general.Point;
import bomber.player.Player;
import lombok.Getter;

public class Game {
    @Getter
    private int joinedPlayers;
    @Getter
    private final GameField field;
    @Getter
    private final Player[] players;
    private final Settings settings;


    public Game(GameField field, Settings settings) {
        this.field = field;
        this.joinedPlayers = 0;
        this.settings = settings;
        this.players = new Player[field.getSpawnPoints().length];
    }

    public boolean isFull() {
        return joinedPlayers == players.length;
    }

    public void addPlayer(Player player) {
        this.players[player.getId()] = player;
        this.joinedPlayers++;
    }

    public void removePlayer(Player player) {
        assert(this.players[player.getId()] == player);
        this.players[player.getId()] = null;
        this.joinedPlayers--;
    }

    public Point spawnPoint(int index) {
        return new Point(field.getSpawnPoints()[index]);
    }

    public boolean isFinished() {
        int alive = 0;
        for (Player p : players) {
            if (p != null && p.isAlive()) {
                alive++;
            }
        }
        // Finished if: ( VS battle and only 1 alive) or 0 alive
        return (this.playerSlots() > 1 && alive <= 1) || alive == 0;
    }

    public void gameTick() {
        // Move players
        for (Player p : players) {
            if (p != null) {
                p.move(field);
                if (settings.isTrue("resetWalkOnTick")) {
                    p.setMoving(false);
                }

            }
        }
        // Explode bombs
    }

    ////////////////////////////////////////
    /////// Helper methods basically ///////
    ////////////////////////////////////////
    public int nextFreePlayer() {
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i] == null) return i;
        }
        return -1;
    }

    public int playerSlots() {
        return this.players.length;
    }
}
