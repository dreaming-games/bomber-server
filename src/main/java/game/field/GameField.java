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

    public int getWidth() {
        return this.field[0].length;
    }

    public int getHeight() {
        return this.field.length;
    }



    ////////////////////////////////////////////
    ////// Some convenient toString stuff //////
    ////////////////////////////////////////////

    public String toString(int lineY) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : this.field[lineY]) {
            if (aByte == 0) {
                // Empty spot
                sb.append(" ");
            } else if (aByte == 1) {
                // A wall
                sb.append("#");
            } else if (aByte >= 10 && aByte <= 100) {
                // Crate, filled or not
                sb.append(".");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.field.length; i++) {
            sb.append(toString(i)).append('\n');
        }
        // Return without the last '\n' character
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
