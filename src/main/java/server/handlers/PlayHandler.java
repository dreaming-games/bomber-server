package server.handlers;

import game.general.Direction;
import game.play.Game;
import game.play.Player;
import server.clients.ClientSocket;

public class PlayHandler extends GameHandler {


    PlayHandler(Game game, ClientSocket[] clients) {
        super(clients, game);
        for (ClientSocket client : clients) {
            client.handler = this;
        }
        Thread gameThread = new Thread(this::gameLoop);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    private void gameLoop() {
        int secondsToStart = 3;
        while (!this.game.isFinished()) {
            if (secondsToStart >= 0) {
                safeSleep(1000);
                broadcast("starting " + this.game.getJoinedPlayers()
                        + " " + secondsToStart);
                secondsToStart--;
                continue;
            }

            // 1) play a game tick
            this.game.gameTick();
            // 2) update all clients state
            for (Player p : this.game.getPlayers()) {
                if (p != null) {
                    p.setMoving(false);
                    broadcast("p " + p.getId() + " " + p.getLocation().x
                            + "/" + p.getLocation().y + " " + p.getDirection().toString());
                }
            }

            // Wait until next tick time
            safeSleep(500);
        }
        System.err.println("Game loop finished");
    }

    @Override
    public void onMessage(ClientSocket socket, String msg) {
        String[] msgParts = msg.split(" ", 2);
        Player player = this.game.getPlayers()[socket.inGameId];
        switch (msgParts[0]) {
            case "drop": // Drop a bomb
                System.out.println("Dropping a bomb");
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
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
