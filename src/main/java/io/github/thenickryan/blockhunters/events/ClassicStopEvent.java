package io.github.thenickryan.blockhunters.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClassicStopEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public ClassicStopEvent() {
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
