package bomber.entity;

import lombok.Getter;

class Stats {
    @Getter
    private double moveSpeed;
    @Getter
    private int bombRadius;

    Stats() {
        this.moveSpeed = 0.2;
        this.bombRadius = 4;
    }
}
