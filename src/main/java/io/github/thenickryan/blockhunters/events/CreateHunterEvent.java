package io.github.thenickryan.blockhunters.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CreateHunterEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;

    public CreateHunterEvent(Player p) {
        super();
        this.player = p;
    }

    public Player getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
