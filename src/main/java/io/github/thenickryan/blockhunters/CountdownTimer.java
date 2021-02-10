package io.github.thenickryan.blockhunters;

import io.github.thenickryan.blockhunters.events.NewBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTimer extends BukkitRunnable {

    private final JavaPlugin plugin;
    private int counter;

    public CountdownTimer(JavaPlugin plugin, int counter) {
        this.plugin = plugin;
        if (counter <= 0) {
            throw new IllegalArgumentException("Counter must be greater than 0.");
        } else {
            this.counter = counter;
        }
    }

    @Override
    public void run() {
        switch (counter) {
            case 5:
                plugin.getServer().broadcastMessage(ChatColor.AQUA + "Ready?");
                counter--;
                break;
            case 4:
                counter--;
                break;
            case 3:
                plugin.getServer().broadcastMessage(ChatColor.AQUA + "First block in " + counter-- + "...");
                break;
            case 2:
            case 1:
                plugin.getServer().broadcastMessage(ChatColor.AQUA + "" + counter-- + "...");
                break;
            case 0:
                NewBlockEvent e = new NewBlockEvent();
                plugin.getServer().getPluginManager().callEvent(e);
                this.cancel();
                break;
        }
    }

}
