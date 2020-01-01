package bomber.game;

import bomber.entity.Bomb;
import bomber.events.Event;
import bomber.events.EventType;
import bomber.events.TickEvents;
import bomber.field.GameField;
import bomber.general.Point;
import bomber.entity.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Game {
    @Getter
    private int joinedPlayers;
    @Getter
    private final GameField field;
    @Getter
    private final Player[] players;
    @Getter
    private final ArrayList<Bomb> bombs;
    private final Settings settings;
    private TickEvents currentTickEvents;


    public Game(GameField field, Settings settings) {
        this.field = field;
        this.joinedPlayers = 0;
        this.settings = settings;
        this.bombs = new ArrayList<>(64);
        this.players = new Player[field.getSpawnPoints().length];
        this.currentTickEvents = new TickEvents(0);
    }

    ///////////////////////////////////////
    /////// Pre / post game methods ///////
    ///////////////////////////////////////

    public synchronized boolean isFull() {
        return joinedPlayers == players.length;
    }

    public synchronized void addPlayer(Player player) {
        this.players[player.getId()] = player;
        this.joinedPlayers++;
    }

    public synchronized void removePlayer(Player player) {
        assert(this.players[player.getId()] == player);
        this.players[player.getId()] = null;
        this.joinedPlayers--;
    }

    public synchronized Point spawnPoint(int index) {
        return new Point(field.getSpawnPoints()[index]);
    }

    /////////////////////////////////
    /////// Game play methods ///////
    /////////////////////////////////

    public synchronized boolean isFinished() {
        int alive = 0;
        for (Player p : players) {
            if (p != null && p.isAlive()) {
                alive++;
            }
        }
        // Finished if: ( VS battle and only 1 alive) or 0 alive
        return (this.playerSlots() > 1 && alive <= 1) || alive == 0;
    }

    public synchronized TickEvents gameTick() {
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
        for (Bomb b : bombs) {
            if (!b.isExploded() && b.tick()) {
                explode(b);
            }
        }

        // Return events from this tick and make new one for next tick
        TickEvents thisTickEvents = currentTickEvents;
        currentTickEvents = new TickEvents(thisTickEvents.getTickNum() + 1);
        return thisTickEvents;
    }

    private void explode(Bomb exploded) {
        LinkedList<Bomb> hit = new LinkedList<>();
        exploded.setExploded(true);
        hit.push(exploded);

        while (!hit.isEmpty()) {
            Bomb b = hit.pollFirst();

            // Bomb exploded!
            currentTickEvents.getEvents().add(new Event<>(
                    EventType.BOMB_BOOM, b));

            // Check if players got hurt / died
//            for (Player p : players) {
//                if (b.inBlast(p.getLocation(), 0.4)) {
//                    currentTickEvents.getEvents().add(new Event<>(
//                            EventType.PLAYER_HURT, p));
//                }
//            }

            // Check all other bombs (chain explosion?)
            for (Bomb b2 : bombs) {
                if (b2.isExploded()) continue;
                if (b.inBlast(new Point(b2.getLocX() + 0.5,
                        b2.getLocY() + 0.5), 0.2)) {
                    b2.setExploded(true);
                    hit.push(b2);
                }
            }

        }
    }


    public synchronized void dropBomb(Player player) {
        Bomb b = new Bomb(player, settings
                .getInt("ticksPerSec") * 3);
        this.bombs.add(b);
        currentTickEvents.getEvents().add(new Event<>(
                EventType.BOMB_DROP, b));
    }

    ////////////////////////////////////////
    /////// Helper methods basically ///////
    ////////////////////////////////////////

    public synchronized int nextFreePlayer() {
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i] == null) return i;
        }
        return -1;
    }

    public synchronized int playerSlots() {
        return this.players.length;
    }
}
