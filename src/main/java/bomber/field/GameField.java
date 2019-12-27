package bomber.field;

import bomber.general.Direction;
import bomber.general.Point;
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

    //////////////////////////////////////
    ////// Checking wall collisions //////
    //////////////////////////////////////

    public void undoCollide(Point loc, double radius, Direction dirMoved,
                            boolean checkX, boolean checkY) {
        int leftBox = (int) (loc.x - radius);
        if (leftBox < 0) leftBox = 0;

        int rightBox = (int) (loc.x + radius);
        if (rightBox >= field[0].length) rightBox = field[0].length;

        int topBox = (int) (loc.y - radius);
        if (topBox < 0) topBox = 0;

        int botBox = (int) (loc.y + radius);
        if (botBox >= field.length) botBox = field.length;

        Direction moveBack = dirMoved.opposite;
        for (int xBox = leftBox; xBox <= rightBox; xBox++) {
            for (int yBox = topBox; yBox <= botBox; yBox++) {
                // Go over all squares that this location (w/radius) is in.
                byte val = field[yBox][xBox];
                if (val == 1 || val == 10) {
                    // if wall or crate, move back by the amount of overlap:
                    if (checkX) {
                        double xOverlap = Math.abs( Math.abs( (loc.x) - (xBox + 0.5) ) - 0.5 - radius );
                        if (xOverlap > 0.001 && moveBack.xDir != 0) {
                            moveBack.moveX(loc, xOverlap * 1.05);
                        }
                    }
                    if (checkY) {
                        double yOverlap = Math.abs( Math.abs( (loc.y) - (yBox + 0.5) ) - 0.5 - radius );
                        if (yOverlap > 0.001 && moveBack.yDir != 0) {
                            moveBack.moveY(loc, yOverlap * 1.05);
                        }
                    }
                }
            }
        }
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
