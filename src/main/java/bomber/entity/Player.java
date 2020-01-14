package bomber.entity;

import bomber.field.GameField;
import bomber.general.Direction;
import bomber.general.Point;
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
    @Getter
    private int invulnerable;
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
        this.lives = 3;
    }

    public void takeDamage(int damage) {
        this.lives -= damage;
        // Todo: set the time someone is invulnerable
        this.invulnerable = 40;
    }

    public void tick(GameField field) {
        if (this.invulnerable >= 0) {
            this.invulnerable--;
        }

        if (moving) {
            Direction d = this.direction;
            // Move X and fix collision, then same for Y
            d.moveXScale(this.location, this.stats.getMoveSpeed());
            field.undoCollide(location, 0.4, d, true, false);
            d.moveYScale(this.location, this.stats.getMoveSpeed());
            field.undoCollide(location, 0.4, d, false, true);
        }
    }

    public boolean isAlive() {
        return this.lives > 0;
    }

}
