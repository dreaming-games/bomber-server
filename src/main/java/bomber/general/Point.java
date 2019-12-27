package bomber.general;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Point {
    public double x;
    public double y;

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }
}
