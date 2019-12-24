package game.general;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Point {
    public double x;
    public double y;

    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }
}
