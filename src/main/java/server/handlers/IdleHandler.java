package server.handlers;

import server.clients.ClientSocket;

public class IdleHandler implements ClientHandler {
    private JoinHandler joiningGame;

    @Override
    public void onMessage(ClientSocket client, String msg) {
        String[] msgParts = msg.split(" ", 2);
        switch (msgParts[0]) {
            case "join":
                if (this.joiningGame == null || this.joiningGame.isFull()) {
                    this.joiningGame = new JoinHandler();
                }
                this.joiningGame.join(client, msgParts[1]);
                break;
            case "practice":
                // Todo
                break;
        }
    }
}
