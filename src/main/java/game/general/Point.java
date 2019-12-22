package game.general;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point {
    private double x;
    private double y;

    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }
}
