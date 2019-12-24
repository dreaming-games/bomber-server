package game.general;

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

    private double xDir, yDir;
    Direction(double x, double y) {
        this.xDir = x;
        this.yDir = y;
    }

    public void move(Point point, double scalar) {
        point.x += (this.xDir * scalar);
        point.y += (this.yDir * scalar);
    }
}
