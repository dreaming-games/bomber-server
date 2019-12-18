package game.play;

import game.general.Direction;
import game.general.Point;
import lombok.Getter;
import lombok.Setter;

public class Player {
    @Setter @Getter
    private Point location;
    @Setter @Getter
    private Direction direction;


    public Player(Point location, String name) {
        this.location = location;
        // South so that we all look down I guess
        this.direction = Direction.SOUTH;
    }
}
