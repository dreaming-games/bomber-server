package game.field;

import game.general.Point;
import lombok.Getter;

import java.util.ArrayList;

public class GameField {
    @Getter
    private final Point[] spawnPoints;
    private final byte[][] field;

    GameField(ArrayList<byte[]> byteLines, ArrayList<Point> spawnPoints) {
        this.spawnPoints = new Point[spawnPoints.size()];
        if (spawnPoints.toArray(this.spawnPoints) != this.spawnPoints) {
            throw new RuntimeException("We done fucked up in constructor...");
        }
        this.field = new byte[byteLines.size()][];
        if (byteLines.toArray(this.field) != this.field) {
            throw new RuntimeException("We done fucked up in constructor...");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte[] bytes : this.field) {
            for (byte aByte : bytes) {
                switch (aByte) {
                    case 0:
                        sb.append("  ");
                        break;
                    case 1:
                        sb.append("##");
                        break;
                    case 10:
                        sb.append("[]");
                        break;
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
