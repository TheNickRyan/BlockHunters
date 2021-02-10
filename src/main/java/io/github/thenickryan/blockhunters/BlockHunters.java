package io.github.thenickryan.blockhunters;

import io.github.thenickryan.blockhunters.commands.CommandManager;
import io.github.thenickryan.blockhunters.events.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BlockHunters extends JavaPlugin {

    public static List<String> BANNED_MATERIALS = new ArrayList<>();
    public static List<String> OVERWORLD_MATERIALS = new ArrayList<>();
    public static List<String> NETHER_MATERIALS = new ArrayList<>();
    public static List<String> END_MATERIALS = new ArrayList<>();
    public static List<String> LEGENDARY_MATERIALS = new ArrayList<>();
    public static List<String> EPIC_MATERIALS = new ArrayList<>();
    public static List<String> RARE_MATERIALS = new ArrayList<>();
    public static boolean GAME_STATE = false;
    public static String GAME_TYPE = "";

    @Override
    public void onEnable() {
        loadFromConfig();
        getServer().getPluginManager().registerEvents(new EventManager(this), this);
        this.getCommand("blockhunters").setExecutor(new CommandManager());
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Plugin is disabled");
    }

    public static void setGameState(boolean gameState) {
        GAME_STATE = gameState;
    }

    public static boolean getGameState() {
        return GAME_STATE;
    }

    public static void setGameType(String gameType) {
        GAME_TYPE = gameType;
    }

    public static String getGameType() {
        return GAME_TYPE;
    }

    public void loadFromConfig() {
        RARE_MATERIALS.addAll(this.getConfig().getStringList("materials.rare"));
        EPIC_MATERIALS.addAll(this.getConfig().getStringList("materials.epic"));
        LEGENDARY_MATERIALS.addAll(this.getConfig().getStringList("materials.legendary"));
        OVERWORLD_MATERIALS.addAll(this.getConfig().getStringList("materials.overworld"));
        NETHER_MATERIALS.addAll(this.getConfig().getStringList("materials.nether"));
        END_MATERIALS.addAll(this.getConfig().getStringList("materials.end"));
        BANNED_MATERIALS.addAll(this.getConfig().getStringList("materials.banned"));
        BANNED_MATERIALS.addAll(RARE_MATERIALS);
        BANNED_MATERIALS.addAll(EPIC_MATERIALS);
        BANNED_MATERIALS.addAll(LEGENDARY_MATERIALS);
        BANNED_MATERIALS.addAll(OVERWORLD_MATERIALS);
        BANNED_MATERIALS.addAll(NETHER_MATERIALS);
        BANNED_MATERIALS.addAll(END_MATERIALS);
    }
}
