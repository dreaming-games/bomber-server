package bomber.entity;

import lombok.Data;

@Data
class Stats {
    private int bombsAtATime;
    private double moveSpeed;
    private int bombRadius;

    Stats() {
        this.bombsAtATime = 2;
        this.moveSpeed = 0.2;
        this.bombRadius = 3;
    }
}
