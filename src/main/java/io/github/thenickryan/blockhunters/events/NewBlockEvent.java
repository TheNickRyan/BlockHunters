package io.github.thenickryan.blockhunters.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class NewBlockEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public NewBlockEvent() {
        super();
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}