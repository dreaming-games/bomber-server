package game.general;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point {
    private int x;
    private int y;

    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }
}
