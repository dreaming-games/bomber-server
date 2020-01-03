package bomber.field;

import bomber.general.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Iterator;

@AllArgsConstructor
public class Square implements Iterable<Square> {
    @Getter
    private final int x, y, w, h;

    private Square(int x, int y) {
        this(x, y, 1, 1);
    }

    public Square(Point p) {
        this((int) p.x, (int) p.y);
    }

    public boolean intersects(Point loc, double radius) {
        return (loc.x + radius > x && loc.x - radius < x + w) && // Hits it horizontally
                (loc.y + radius > y && loc.y - radius < y + h);  // Hits it vertically
    }

    @Override
    public String toString() {
        return x + " " + y + " " + w + " " + h;
    }

    @Override
    public Iterator<Square> iterator() {
        return new Iterator<>() {
            private int atX = x;
            private int atY = y;

            @Override
            public boolean hasNext() {
                return atY < y + h;
            }

            @Override
            public Square next() {
                Square s = new Square(atX, atY, 1, 1);
                if (++atX == x + w) {
                    atX = x;
                    atY++;
                }
                return s;
            }
        };
    }
}
