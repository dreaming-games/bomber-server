package bomber.field;

import bomber.general.Point;
import lombok.Getter;

public class Square {
    @Getter
    private final int x, y, w, h;

    public Square(int x, int y) {
        this(x, y, 1, 1);
    }

    public Square(Point p) {
        this((int) p.x, (int) p.y);
    }

    public Square(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
