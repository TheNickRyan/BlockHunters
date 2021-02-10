package io.github.thenickryan.blockhunters.events;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class PvpStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final List<Player> players;
    private final CommandSender sender;

    public PvpStartEvent(CommandSender sender) {
        List<World> worldList = sender.getServer().getWorlds();
        this.players = !worldList.isEmpty() ? worldList.get(0).getPlayers() : null;
        this.sender = sender;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public CommandSender getSender() {
        return this.sender;
    }
}
