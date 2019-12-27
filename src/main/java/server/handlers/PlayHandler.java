package server.handlers;

import bomber.general.Direction;
import bomber.player.Player;
import server.clients.ClientSocket;

public class PlayHandler implements ClientHandler {
    private final GameHandle gameHandle;

    PlayHandler(GameHandle gameHandle) {
        this.gameHandle = gameHandle;
        for (ClientSocket client : gameHandle.clients) {
            client.handler = this;
        }
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
            long tickStart = System.currentTimeMillis();

            // 1) play a game tick
            this.gameHandle.game.gameTick();
            long sendStart = System.currentTimeMillis();
            // 2) update all clients state
            for (Player p : this.gameHandle.game.getPlayers()) {
                if (p != null) {
                    this.gameHandle.broadcast("p " + p.getId() + " " + p.getLocation().x
                            + "/" + p.getLocation().y + " " + p.getDirection().toString());
                }
            }

            tickWait(tickStart, sendStart);
        }
        System.err.println("Game loop finished");
    }

    @Override
    public void onMessage(ClientSocket socket, String msg) {
        String[] msgParts = msg.split(" ", 2);
        Player player = this.gameHandle.game.getPlayers()[socket.inGameId];
        switch (msgParts[0]) {
            case "drop": // Drop a bomb
                System.out.println("Dropping a bomb");
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
