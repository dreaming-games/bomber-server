package game.field;

import game.general.Point;

import java.util.ArrayList;

public class GameField {
    private final Point[] spawnPoints;
    private final byte[][] field;

    GameField(ArrayList<byte[]> byteLines, ArrayList<Point> spawnPoints) {
        this.spawnPoints = new Point[spawnPoints.size()];
        if (spawnPoints.toArray(this.spawnPoints) != this.spawnPoints) {
            System.err.println("We done fucked up in constructor...");
        }
        this.field = new byte[byteLines.get(0).length][byteLines.size()];
        if (byteLines.toArray(this.field) != this.field) {
            System.err.println("We done fucked up in constructor...");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < this.field.length; x++) {
            for (int y = 0; y < this.field.length; y++) {
                System.out.println(field[y]);
                sb.append(field[y][x]);
            }
        }
        return sb.toString();
    }
}
