package blockhunters.events;

import org.bukkit.event.HandlerList;

public class DifficultyIncreaseEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public DifficultyIncreaseEvent() {
        super();
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
