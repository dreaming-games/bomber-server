package bomber.events;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TickEvents {
    private final int tickNum;
    private final ArrayList<Event> events;

    public TickEvents(int tick) {
        this.tickNum = tick;
        this.events = new ArrayList<>();
    }
}
