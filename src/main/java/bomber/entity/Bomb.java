package bomber.entity;

import bomber.field.GameField;
import bomber.field.Square;
import bomber.general.Point;
import lombok.Data;

@Data
public class Bomb {
    private final Square location;
    private final int playerId;
    private final int radius;

    private int locX, locY;
    private Square[] blast;
    private int ticksToBoom;
    private boolean exploded;

    public Bomb(Player dropper, int ticksToBoom) {
        this.location = new Square(dropper.getLocation());
        this.radius = dropper.getStats().getBombRadius();
        this.playerId = dropper.getId();

        this.locX = (int) dropper.getLocation().x;
        this.locY = (int) dropper.getLocation().y;

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
        for (; startX > location.getX() - radius; startX--) {
            switch (field.getField()[location.getY()][startX]) {
                case 1:
            }
        }

        this.blast[0] = new Square(location.getX() - radius, location.getY(), radius * 2 + 1, 1);
        this.blast[1] = new Square(location.getX(), location.getY() - radius, 1, radius * 2 + 1);
    }

    public boolean inBlast(Point loc, double radius) {
        if (    (loc.x + radius > locX - this.radius && loc.x - radius < locX + this.radius) // In right column(s)
            &&  (loc.y + radius > locY          && loc.y - radius < locY + 1)  ) { // In the right row!
            return true; // In horizontal rect
        }
        if (    (loc.x + radius > locX          && loc.x - radius < locX + 1)           // In the right column
            &&  (loc.y + radius > locY - this.radius && loc.y - radius < locY + this.radius)  ) { // In the right row(s)
            return true; // In vertical rect
        }
        return false;
    }
}
