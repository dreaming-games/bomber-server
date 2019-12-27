package bomber.general;

public enum Direction {
    N(0.0, -1.0), // Up -> Negative Y
    NE(Constants.DIAGONAL, -Constants.DIAGONAL),
    E(1.0, 0.0),
    SE(Constants.DIAGONAL, Constants.DIAGONAL),
    S(0.0, 1.0),
    SW(-Constants.DIAGONAL, Constants.DIAGONAL),
    W(-1.0, 0.0),
    NW(-Constants.DIAGONAL, -Constants.DIAGONAL);

    private static final class Constants {
        // A^2 + B^2 = C^2 -> D^2 + D^2 = 1^2 -> D^2 = 0.5
        static final double DIAGONAL = Math.sqrt(0.5);
    }

    static {
        N.opposite = S;
        NE.opposite = SW;
        E.opposite = W;
        SE.opposite = NW;
        S.opposite = N;
        SW.opposite = NE;
        W.opposite = E;
        NW.opposite = SE;
    }

    public Direction opposite;
    public final double xDir, yDir;
    Direction(double x, double y) {
        this.xDir = x;
        this.yDir = y;
    }






    public void moveXScale(Point point, double scalar) {
        point.x += (this.xDir * scalar);
    }

    public void moveYScale(Point point, double scalar) {
        point.y += (this.yDir * scalar);
    }

    public void moveX(Point point, double amount) {
        if (this.xDir > 0) {
            point.x += amount;
        } else if (this.xDir < 0) {
            point.x -= amount;
        }
    }

    public void moveY(Point point, double amount) {
        if (this.yDir > 0) {
            point.y += amount;
        } else if (this.yDir < 0) {
            point.y -= amount;
        }
    }
}
