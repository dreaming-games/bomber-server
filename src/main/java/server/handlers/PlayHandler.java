package server.handlers;

import bomber.entity.Bomb;
import bomber.events.Event;
import bomber.events.TickEvents;
import bomber.field.Square;
import bomber.general.Direction;
import bomber.entity.Player;
import server.clients.ClientSocket;
import server.main.Server;
import general.Runnable;

public class PlayHandler implements ClientHandler {
    private final Runnable<PlayHandler> onFinished;
    final GameHandle gameHandle;

    PlayHandler(GameHandle gameHandle, Runnable<PlayHandler> onFinished) {
        this.gameHandle = gameHandle;
        this.onFinished = onFinished;
        gameHandle.setClientHandler(this);
        // Start the actual game loop on a new thread
        Thread gameThread = new Thread(this::gameLoop);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    private void gameLoop() {
        for (int i = 3; i >= 0; i--) {
            this.gameHandle.broadcast("starting " + this.gameHandle
                    .game.getJoinedPlayers() + " " + i);
            safeSleep(1000);
        }

        while (!this.gameHandle.game.isFinished()) {
            // 1) play a game tick
            long tickStart = System.currentTimeMillis();
            TickEvents tick = this.gameHandle.game.gameTick();

            // 2) update all clients states
            long sendStart = System.currentTimeMillis();
            updateClients(tick);

            // 3) Wait for the tick to end (either time or messages)
            tickWait(tickStart, sendStart);
        }

        if (this.gameHandle.game.playerSlots() <= 1) {
            this.gameHandle.broadcast("over");
        } else {
            // Let everybody know the winner
            this.gameHandle.broadcast("over "
                    + this.gameHandle.game.winnerId());
        }

        System.err.println("Game loop finished");
        gameHandle.setClientHandler(Server.idleHandler);
        onFinished.run(this);
    }

    ////////////////////////////////////////
    ////// Client communication stuff //////
    ////////////////////////////////////////

    private void updateClients(TickEvents tick) {
        this.gameHandle.broadcast("tick " + tick.getTickNum());

        // Send all player locations
        for (Player p : this.gameHandle.game.getPlayers()) {
            if (p != null) {
                this.gameHandle.broadcast("p " + p.getId() + " " + p.getLocation().x
                        + "/" + p.getLocation().y + " " + p.getDirection().toString());
            }
        }

        // Send all events that happened
        for (Event event : tick.getEvents()) {
            switch (event.getType()) {
                case BOMB_BOOM: {
                    Bomb b = (Bomb) event.getData();
                    this.gameHandle.broadcast("boom " + b.toString());
                    for (Square blast : b.getBlast()) {
                        this.gameHandle.broadcast("fire " + blast);
                    }
                    break;
                }
                case BOMB_DROP: {
                    Bomb b = (Bomb) event.getData();
                    this.gameHandle.broadcast("drop " + b.toString()
                            + " " + b.getPlayerId());
                    break;
                }
                case PLAYER_DIED: {
                    Player p = (Player) event.getData();
                    this.gameHandle.broadcast("died "
                            + p.getId());
                    break;
                }
                case PLAYER_HURT: {
                    Player p = (Player) event.getData();
                    this.gameHandle.broadcast("hurt "
                            + p.getId() + " " + p.getLives());
                    break;
                }
                case CRATE_DESTROY: {
                    Square s = (Square) event.getData();
                    // double space at end, 1 for separator, 1 for
                    // new thing in map: empty spot = space
                    this.gameHandle.broadcast("change "
                            + s.getX() + "/" + s.getY() + "  ");
                    break;
                }
            }
        }

    }

    @Override
    public void onMessage(ClientSocket socket, String msg) {
        String[] msgParts = msg.split(" ", 2);
        Player player = this.gameHandle.game.getPlayers()[socket.inGameId];
        switch (msgParts[0]) {
            case "drop": // Drop a bomb
                this.gameHandle.game.dropBomb(player);
                break;
            case "stop": // Stop moving!
                player.setMoving(false);
                break;
            case "move": // Move in a certain direction
                try {
                    Direction d = Direction.valueOf(msgParts[1]);
                    player.setDirection(d);
                    player.setMoving(true);
                } catch (IllegalArgumentException e) {
                    System.err.println("Someone sending bs direction " + msgParts[1]);
                }
                break;
        }
    }

    @Override
    public void onConnectionClose(ClientSocket socket) {
        // Todo: tell people that someone left?
    }

    /////////////////////////////////////
    ////// Helper (timing) methods //////
    /////////////////////////////////////

    private void safeSleep(long millis) {
        try {
            if (millis <= 0) return;
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tickWait(long tickStart, long sendStart) {
        // Wait until next tick time
        final int ticksPerSecond = 30;
        final long tickMillis = 1000 / ticksPerSecond;
        final long time = System.currentTimeMillis();
        final long tickTime = time - tickStart;
        final long sendTime = time - sendStart;
        final long waitTime = tickMillis - tickTime;
        if (waitTime < 5) {
            System.err.println("Cutting it close are we? This tick took "
                    + tickTime + "ms (" + (tickTime - sendTime) + ","
                    + sendTime + "), only waiting " + waitTime + "ms!");
        }
        safeSleep(waitTime);
    }
}
