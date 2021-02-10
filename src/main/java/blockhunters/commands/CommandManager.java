package blockhunters.commands;

import blockhunters.BlockHunters;
import blockhunters.events.ClassicStartEvent;
import blockhunters.events.ClassicStopEvent;
import blockhunters.events.EventManager;
import blockhunters.events.PvpStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    /**********************************************************
     * Main Command Tree
     **********************************************************/

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player-only command!");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("blockhunters")) {
            switch (args[0]) {
                case "classic":
                    if (!BlockHunters.getGameState()) {
                        BlockHunters.setGameState(true);
                        BlockHunters.setGameType("classic");
                        ClassicStartEvent e = new ClassicStartEvent(player);
                        Bukkit.getPluginManager().callEvent(e);
                    }
                    else {
                        player.sendMessage(ChatColor.DARK_RED + "Error: A game is currently running.");
                    }
                    break;
                case "pvp":
                    if (!BlockHunters.getGameState()) {
                        BlockHunters.setGameState(true);
                        BlockHunters.setGameType("pvp");
                        PvpStartEvent e = new PvpStartEvent(player);
                        Bukkit.getPluginManager().callEvent(e);
                    }
                    else {
                        player.sendMessage(ChatColor.DARK_RED + "Error: A game is currently running.");
                    }
                    break;
                case "lives":
                    if (BlockHunters.getGameState()) {
                        for (Player p : EventManager.players) {
                            if (p == player) {
                                player.sendMessage(ChatColor.AQUA + "Lives: " + ChatColor.GOLD + EventManager.lives.get(p));
                            }
                        }
                    }
                    break;
                case "block":
                    if (BlockHunters.getGameState()) {
                        for (Player p : EventManager.players) {
                            if (p == player) {
                                player.sendMessage(ChatColor.AQUA + "Your block: " + ChatColor.GOLD + EventManager.blockNames.get(EventManager.currentSearchBlock.get(player)) + ".");
                            }
                        }
                    }
                    break;
                case "stop":
                    BlockHunters.setGameState(false);
                    BlockHunters.setGameType("");
                    player.getServer().getPluginManager().callEvent(new ClassicStopEvent());
                    player.sendMessage("BlockHunters has been stopped.");
                    break;
                case "compass":
                    if (BlockHunters.getGameState()) {
                        if (!player.getInventory().contains(EventManager.compass)) {
                            if (EventManager.players.contains(player)) {
                                EventManager.getCompass(player);
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.DARK_RED + "You already have a compass.");
                        }
                    }
                    else {
                        player.sendMessage(ChatColor.DARK_RED + "Error: A game is not currently running.");
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
        return true;
    }
}
