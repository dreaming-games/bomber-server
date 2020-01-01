package bomber.events;

import lombok.Data;

@Data
public class Event<T> {
    private final EventType type;
    private final T data;

    public Event(EventType type, T data) {
        this.type = type;
        this.data = data;
    }
}
