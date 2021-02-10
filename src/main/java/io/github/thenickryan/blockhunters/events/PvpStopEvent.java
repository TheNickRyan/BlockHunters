package io.github.thenickryan.blockhunters.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PvpStopEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public PvpStopEvent() {
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
