package game.play;

import game.general.Direction;
import game.general.Point;
import lombok.Getter;
import lombok.Setter;

public class Player {
    @Getter
    private final int id;
    @Getter
    private String name;
    @Getter
    private Stats stats;
    @Setter @Getter
    private Point location;
    @Setter @Getter
    private Direction direction;


    public Player(int id, String name, Point location) {
        this.id = id;
        this.name = name;
        this.location = location;
        // South so that we all look down I guess
        this.direction = Direction.SOUTH;
        this.stats = new Stats();
    }
}
