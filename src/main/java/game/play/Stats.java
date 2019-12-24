package game.play;

import lombok.Getter;

class Stats {
    @Getter
    private double moveSpeed;

    Stats() {
        this.moveSpeed = 0.1;
    }
}
