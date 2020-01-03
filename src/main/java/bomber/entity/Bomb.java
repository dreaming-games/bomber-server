package bomber.entity;

import bomber.field.GameField;
import bomber.field.MapObject;
import bomber.field.Square;
import bomber.general.Point;
import lombok.Data;
import lombok.Getter;

@Data
public class Bomb {
    @Getter
    private final Square location;
    @Getter
    private final int playerId;
    @Getter
    private final int radius;

    @Getter
    private Square[] blast;
    @Getter
    private int ticksToBoom;
    @Getter
    private boolean exploded;

    public Bomb(Player dropper, int ticksToBoom) {
        this.location = new Square(dropper.getLocation());
        this.radius = dropper.getStats().getBombRadius();
        this.playerId = dropper.getId();

        this.ticksToBoom = ticksToBoom;
        this.exploded = false;
    }

    public boolean tick() {
        return (--this.ticksToBoom) <= 0;
    }

    public void calculateBlast(GameField field) {
        // lets assume a standard X explosion bomb for now
        this.blast = new Square[2];

        int startX = location.getX();
        loop: while (startX >= location.getX() - radius + 1) {
            startX--;
            switch (field.getField()[location.getY()][startX]) {
                case MapObject.WALL:
                    // Go one to the back again, then break
                    startX++;
                    break loop;
                case MapObject.CRATE:
                    // Explode on the crate as well, so just break
                    break loop;
            }
        }

        int startY = location.getY();
        loop: while (startY >= location.getY() - radius + 1) {
            startY--;
            switch (field.getField()[startY][location.getX()]) {
                case MapObject.WALL:
                    // Go one to the back again, then break
                    startY++;
                    break loop;
                case MapObject.CRATE:
                    // Explode on the crate as well, so just break
                    break loop;
            }
        }

        // So the blast will start (left) at startX and (top) at startY, now the ends:

        int endX = location.getX();
        loop: while (endX <= location.getX() + radius - 1) {
            endX++;
            switch (field.getField()[location.getY()][endX]) {
                case MapObject.WALL:
                    // Go one to the back again, then break
                    endX--;
                    break loop;
                case MapObject.CRATE:
                    // Explode on the crate as well, so just break
                    break loop;
            }
        }

        int endY = location.getY();
        loop: while (endY <= location.getY() + radius - 1) {
            endY++;
            switch (field.getField()[endY][location.getX()]) {
                case MapObject.WALL:
                    // Go one to the back again, then break
                    endY--;
                    break loop;
                case MapObject.CRATE:
                    // Explode on the crate as well, so just break
                    break loop;
            }
        }

        int blastW = endX - startX + 1;
        int blastH = endY - startY + 1;

        this.blast[0] = new Square(startX, location.getY(), blastW, 1);
        this.blast[1] = new Square(location.getX(), startY, 1, blastH);
    }

    public boolean inBlast(Point loc, double radius) {
        for (Square bl : blast) {
            if (bl.intersects(loc, radius)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return this.getLocation().getX() + " " + this.getLocation().getY();
    }
}
