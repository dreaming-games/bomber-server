package bomber.game;

import bomber.entity.Bomb;
import bomber.events.Event;
import bomber.events.EventType;
import bomber.events.TickEvents;
import bomber.field.GameField;
import bomber.field.Square;
import bomber.general.Point;
import bomber.entity.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static bomber.field.MapObject.CRATE;
import static bomber.field.MapObject.EMPTY;

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
        assert(field.getSpawnPoints().length > 0);
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
        assert(player != null);
        assert(this.players[player.getId()] == player);
        this.players[player.getId()] = null;
        this.joinedPlayers--;
    }

    public synchronized Point spawnPoint(int index) {
        return new Point(field.getSpawnPoints()[index]);
    }

    public boolean hasStarted() {
        return this.currentTickEvents.getTickNum() > 0;
    }

    /////////////////////////////////
    /////// Game play methods ///////
    /////////////////////////////////

    public synchronized boolean isOnGoing() {
        int alive = 0;
        for (Player p : players) {
            if (p != null && p.isAlive()) {
                alive++;
            }
        }
        // Ongoing if: solo game and > 0 alive OR > 1 alive
        return (this.playerSlots() <= 1 && alive > 0) || alive > 1;
    }

    public synchronized int winnerId() {
        if (isOnGoing()) return -1;
        for (Player p : players) {
            if (p != null && p.isAlive()) {
                return p.getId();
            }
        }
        return -2;
    }

    public synchronized TickEvents gameTick() {
        // Move players
        for (Player p : players) {
            if (p != null) {
                p.tick(field);
                if (settings.isTrue("resetWalkOnTick")) {
                    p.setMoving(false);
                }
            }
        }

        // Explode bombs
        explode();

        // Return events from this tick and make new one for next tick
        TickEvents thisTickEvents = currentTickEvents;
        currentTickEvents = new TickEvents(thisTickEvents.getTickNum() + 1);
        return thisTickEvents;
    }

    private synchronized void explode() {
        if (bombs.isEmpty()) return;
        ArrayList<Bomb> boom = new ArrayList<>();

        // All bombs that are supposed to go off this tick or are 'exploding'
        Iterator<Bomb> bit = bombs.iterator();
        while (bit.hasNext()) {
            Bomb b = bit.next();
            // If exploding, either check again or remove when done
            if (b.getExploding() > 0) {
                if (b.explodeTick()) {
                    bit.remove();
                    continue;
                } else {
                    boom.add(b);
                }
            }
            // Tick... Tick... Boom?
            if (b.tick()) {
                b.explode(field);
                boom.add(b);
            }
        }

        // Put all chain explosion bombs in boom
        for (int at = 0; at < boom.size(); at++) {
            Bomb b = boom.get(at);
            for (Bomb b2 : bombs) {
                if (b2.getExploding() >= 0) continue;
                if (b.inBlast(new Point(b2.getLocation().getX() + 0.5,
                        b2.getLocation().getY() + 0.5), 0.2)) {
                    b2.explode(field);
                    boom.add(b2);
                }
            }
        }

        // For all these exploded bombs:
        for (Bomb b : boom) {
            // Add events for them
            currentTickEvents.getEvents().add(new Event<>(
                    EventType.BOMB_BOOM, b));

            for (Square blastRect : b.getBlast()) {
                // Check which crates were destroyed
                for (Square single : blastRect) {
                    if (field.getXY(single) == CRATE) {
                        currentTickEvents.getEvents().add(new Event<>(
                                EventType.CRATE_DESTROY, single));
                        // Todo: put down upgrades and stuff
                        field.setXY(single, EMPTY);
                    }
                }
            }
        }

        // Check which players got hurt
        for (Player player : players) {
            if (player == null) continue;
            if (player.getInvulnerable() >= 0) continue;
            for (Bomb b : boom) {
                if (b.inBlast(player.getLocation(), 0.4)) {
                    player.takeDamage(1);
                    currentTickEvents.getEvents().add(new Event<>(
                            EventType.PLAYER_HURT, player));
                    if (player.getLives() <= 0) {
                        currentTickEvents.getEvents().add(new Event<>(
                                EventType.PLAYER_DIED, player));
                    }
                    break;
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
