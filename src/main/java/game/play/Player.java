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
    @Getter
    private int lives;
    @Setter @Getter
    private Point location;
    @Setter @Getter
    private Direction direction;
    @Setter @Getter
    private boolean moving;


    public Player(int id, String name, Point location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.moving = false;
        // South so that we all look down I guess
        this.direction = Direction.S;
        this.stats = new Stats();
        // All start with 3 lives
        // Todo: config file for stats and lives?
        this.lives = 3;
    }

    public void takeDamage(int damage) {
        this.lives -= damage;
    }

    boolean isAlive() {
        return this.lives > 0;
    }

    void move() {
        if (moving) {
            this.direction.move(this.location,
                    this.stats.getMoveSpeed());
        }
    }
}
