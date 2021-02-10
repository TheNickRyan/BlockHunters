package io.github.thenickryan.blockhunters;

import io.github.thenickryan.blockhunters.events.CheckBlockEvent;
import io.github.thenickryan.blockhunters.events.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BackgroundTimer extends BukkitRunnable {

    private final JavaPlugin plugin;
    private int counter;
    private int runTime;

    public BackgroundTimer(JavaPlugin plugin, int counter) {
        this.plugin = plugin;
        if (counter <= 0) {
            throw new IllegalArgumentException("Counter must be greater than 0.");
        } else {
            this.counter = counter;
            this.runTime = 0;
        }
    }

    @Override
    public void run() {
        if (BlockHunters.getGameState()) {
            switch (counter) {
                case 60:
                case 30:
                    plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + counter-- + ChatColor.DARK_RED + " seconds remaining!");
                    break;
                case 10:
                case 9:
                case 8:
                case 7:
                case 6:
                case 5:
                case 4:
                case 3:
                case 2:
                case 1:
                    plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "" + counter-- + "...");
                    break;
                case 0:
                    runTime++;

                    switch (runTime) {
                        case 3:
                            EventManager.removeRareMaterials();
                            plugin.getServer().broadcastMessage(ChatColor.BLUE + "[Difficulty++]");
                            plugin.getServer().broadcastMessage(ChatColor.BLUE + "Rare and Nether blocks have been added!");
                            break;
                        case 6:
                            EventManager.removeEpicMaterials();
                            plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[Difficulty++]");
                            plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "Epic, End and special Overworld blocks have been added!");
                            break;
                        case 9:
                            EventManager.removeLegendaryMaterials();
                            plugin.getServer().broadcastMessage(ChatColor.GOLD + "[Difficulty++]");
                            plugin.getServer().broadcastMessage(ChatColor.GOLD + "Legendary blocks have been added!");
                            break;
                    }

                    plugin.getServer().getPluginManager().callEvent(new CheckBlockEvent());
                    counter = EventManager.timerLength * 60;
                    break;
                default:
                    counter--;
                    break;
            }
        }
    }

}
