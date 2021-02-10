package blockhunters.events;

import blockhunters.BackgroundTimer;
import blockhunters.BlockHunters;
import blockhunters.CountdownTimer;
import blockhunters.items.ItemManager;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

import static blockhunters.BlockHunters.*;

public class EventManager implements Listener {

    /**********************************************************
     * Variables, Lists, Maps, and other fun initializations
     **********************************************************/

    private final BlockHunters plugin;

    public EventManager(BlockHunters plugin) {
        this.plugin = plugin;
    }

    public static final int introTimerLength = 5;
    public static int timerLength;
    public static int gracePeriod;
    public static int lifeCount;
    public static List<Material> blocks = new ArrayList<>();
    public static List<Player> players = new ArrayList<>();
    public static List<Player> hunters = new ArrayList<>();
    public static HashMap<Player, Integer> lives = new HashMap<>();
    public static HashMap<Material, String> blockNames = new HashMap<>();
    public static HashMap<Player, Material> currentSearchBlock = new HashMap<>();
    private static final HashMap<Player, Boolean> foundBlock = new HashMap<>();
    private static final HashMap<Player, Player> compassTargets = new HashMap<>();
    public static final ItemStack compass = ItemManager.createCompass();

    /**********************************************************
     * Initialize Classic game mode
     **********************************************************/

    @EventHandler
    public void onClassicStart(ClassicStartEvent event) {
        //load config
        timerLength = plugin.getConfig().getInt("classic.roundTimer");
        gracePeriod = plugin.getConfig().getInt("classic.gracePeriod");
        lifeCount = plugin.getConfig().getInt("classic.lives");

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.getInventory().clear();
            p.setHealth(20);
            p.setFoodLevel(20);
            p.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
        }

        //Create timer objects and initialize players
        CountdownTimer intro = new CountdownTimer(plugin, introTimerLength);
        BackgroundTimer background = new BackgroundTimer(plugin, timerLength);
        players = event.getPlayers();

        //Generate banned materials
        blocks.addAll(generateMaterialList());
        generateBlockNames();

        //Loop to assign lives to players
        for (Player p : players) {
            lives.put(p, lifeCount);
        }

        //Intro messages
        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Welcome to" + ChatColor.GOLD + " BlockHunters!");
        plugin.getServer().broadcastMessage(ChatColor.AQUA + "You have " + ChatColor.GOLD + gracePeriod + ChatColor.AQUA + " minutes before your first block.");
        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Round length: " + ChatColor.GOLD + timerLength);

        //Start timers for intro and game
        intro.runTaskTimer(plugin, (gracePeriod * 60 * 20) + 40, 20);
        background.runTaskTimer(plugin, (gracePeriod * 60 * 20) + 140, 20);
    }

    /**********************************************************
     * Stop Classic game mode
     **********************************************************/

    @EventHandler
    public void onClassicStop(ClassicStopEvent event) {
        plugin.getServer().broadcastMessage(ChatColor.GOLD + players.get(0).getDisplayName() + " is the winner!");
        BlockHunters.setGameState(false);

        //Clear all game data
        players.clear();
        hunters.clear();
        blocks.clear();
        lives.clear();
        currentSearchBlock.clear();
        foundBlock.clear();
        compassTargets.clear();

        //teleport all players to spawn
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.getInventory().clear();
            p.setHealth(20);
            p.setFoodLevel(20);
            p.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
            p.getWorld().playSound(plugin.getServer().getWorlds().get(0).getSpawnLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 1);
        }
    }

    /**********************************************************
     * Initialize PVP game mode
     **********************************************************/

    @EventHandler
    public void onPvpStart(PvpStartEvent event) {
        //load config
        timerLength = plugin.getConfig().getInt("pvp.roundTimer");
        gracePeriod = plugin.getConfig().getInt("pvp.gracePeriod");
        lifeCount = plugin.getConfig().getInt("pvp.lives");

        //teleport all players to spawn
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.getInventory().clear();
            p.setHealth(20);
            p.setFoodLevel(20);
            p.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
        }

        //Create timer objects and initialize players
        CountdownTimer intro = new CountdownTimer(plugin, introTimerLength);
        BackgroundTimer background = new BackgroundTimer(plugin, timerLength);
        players = event.getPlayers();

        //Generate banned materials
        blocks.addAll(generateMaterialList());
        generateBlockNames();

        //Loop to assign lives and compasses to players
        for (Player p : players) {
            lives.put(p, lifeCount);
            getCompass(p);
        }

        //Intro messages
        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Welcome to" + ChatColor.GOLD + " BlockHunters " + ChatColor.MAGIC + " PVP!");
        plugin.getServer().broadcastMessage(ChatColor.AQUA + "You have " + ChatColor.GOLD + gracePeriod + ChatColor.AQUA + " minutes before your first block.");
        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Round length: " + ChatColor.GOLD + timerLength);

        //Start timers for intro and game
        intro.runTaskTimer(plugin, (gracePeriod * 60 * 20) + 40, 20);
        background.runTaskTimer(plugin, (gracePeriod * 60 * 20) + 140, 20);
    }

    /**********************************************************
     * Stop PVP game mode
     **********************************************************/

    @EventHandler
    public void onPvpStop(PvpStopEvent event) {
        plugin.getServer().broadcastMessage(ChatColor.GOLD + players.get(0).getDisplayName() + " is the winner!");
        BlockHunters.setGameState(false);

        //Clear all game data
        players.clear();
        hunters.clear();
        blocks.clear();
        lives.clear();
        currentSearchBlock.clear();
        foundBlock.clear();
        compassTargets.clear();

        //teleport all players to spawn
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.getInventory().clear();
            p.setHealth(20);
            p.setFoodLevel(20);
            p.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
            p.getWorld().playSound(plugin.getServer().getWorlds().get(0).getSpawnLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 1);
        }
    }

    /**********************************************************
     * Right Click Event
     **********************************************************/

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        //When right-clicking with the compass in hand, switch to the next target in the list
        Player player = event.getPlayer();
        Player target;
        int index;
        ItemStack item = event.hasItem() ? event.getItem() : null;
        if (item != null) {
            if (compass.equals(item)) {
                ItemMeta meta = item.getItemMeta();

                //set index
                index = players.indexOf(compassTargets.get(player));

                //if index is end of player list, loop to beginning and target first player
                if (index == players.size() - 1) {
                    index = 0;
                    //if target is self, increment by 1
                    if (players.get(index).equals(player)) {
                        index++;
                    }
                }

                //otherwise target next player in list
                else {
                    //if target is self and last player in list, set to 0, otherwise increment
                    if (players.get(index).equals(player)) {
                        if (index + 1 == players.size()) {
                            index = 0;
                        }
                        else {
                            index++;
                        }
                    }
                }

                target = players.get(index);

                //call to target player and then set target name for item display name
                targetPlayer(player, target);
                if (meta != null && compassTargets.get(player).equals(target)) {
                    meta.setDisplayName("§6" + target.getDisplayName());
                    item.setItemMeta(meta);
                }
            }
        }
    }

    /**********************************************************
     * Player Move Event
     **********************************************************/

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {

        //update compass target
        Player p = event.getPlayer();
        if (hunters.contains(p) && compassTargets.containsKey(p)) {
            targetPlayer(p, compassTargets.get(p));
        }

        //check if block has been found
        if (!foundBlock.get(p)) {

            Location l = new Location(
                    plugin.getServer().getWorlds().get(0),
                    p.getLocation().getBlockX(),
                    p.getLocation().getBlockY() - 1,
                    p.getLocation().getBlockZ()
            );
            Material block = l.getBlock().getType();

            if (block == currentSearchBlock.get(p)) {
                p.getWorld().playSound(l, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                plugin.getServer().broadcastMessage(ChatColor.GOLD + p.getDisplayName() + ChatColor.AQUA + " has found their block!");
                foundBlock.put(p, true);
            }
        }
    }

    /**********************************************************
     * Entity Portal Enter Event
     **********************************************************/

    @EventHandler
    public void onEntityPortalEnterEvent(EntityPortalEnterEvent event) {

        //secondary win condition, if a player beats the game they automatically win
        if (plugin.getConfig().getBoolean("options.dragon")) {
            Player p;
            if (event.getEntityType().equals(EntityType.PLAYER)) {
                if (Objects.requireNonNull(event.getLocation().getWorld()).getEnvironment().equals(World.Environment.THE_END)) {
                    p = (Player) event.getEntity();
                    plugin.getServer().broadcastMessage(ChatColor.GOLD + "[" + p.getDisplayName() + " has defeated the Ender Dragon]");
                    plugin.getServer().broadcastMessage(ChatColor.GOLD + p.getDisplayName() + " wins!");
                    plugin.getServer().getPluginManager().callEvent(new ClassicStopEvent());
                }
            }
        }
    }

    /**********************************************************
     * Player Death Event
     **********************************************************/

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {

        //find if a player killed another player and calculate lives
        for (Player p : players) {
            if (event.getDeathMessage() != null) {
                if (event.getDeathMessage().contains(p.getDisplayName())) {
                    Player e = event.getEntity();
                    subtractLife(e);
                    addLife(p);
                }
            }
        }
    }

    /**********************************************************
     * New Block Event - Custom Event Class
     **********************************************************/

    @EventHandler
    public void onNewBlock(NewBlockEvent event) {

        //Creates a new block for each player to find, then displays lives and current block to each player
        for (Player p : players) {
            currentSearchBlock.put(p, generateBlock());
            foundBlock.put(p, false);
            p.sendMessage(ChatColor.AQUA + "Lives: " + ChatColor.GOLD + lives.get(p));
            p.sendMessage(ChatColor.AQUA + "Your block: " + ChatColor.GOLD + blockNames.get(currentSearchBlock.get(p)) + ".");
        }
    }

    /**********************************************************
     * Check Block Event - Custom Event Class
     **********************************************************/

    @EventHandler
    public void onCheckBlock(CheckBlockEvent event) {

        //Checks each player to see if they failed to find their block, and if any player ran out of lives
        int counter = 0;
        for (boolean found : foundBlock.values()) {
            if (found) {
                counter++;
            }
        }

        if (counter > 0) {
            for (Player p : players) {

                //Server-wide message displaying any players who failed to find their block
                if (!foundBlock.get(p)) {
                    plugin.getServer().broadcastMessage(ChatColor.GOLD + p.getDisplayName() + ChatColor.DARK_RED + " has failed to find their block.");
                    subtractLife(p);
                }
            }
        }
        else {
            plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "Everyone has failed to find their block.");
        }


        //Trigger newBlockEvent at the end of every check
        plugin.getServer().getPluginManager().callEvent(new NewBlockEvent());
    }

    /**********************************************************
     * Create Hunter Event - Custom Event Class
     **********************************************************/

    @EventHandler
    public void onCreateHunterEvent(CreateHunterEvent event) {
        setHunter(event.getPlayer());
    }

    /**********************************************************
     * Difficulty Increase Event - Custom Event Class
     **********************************************************/

    @EventHandler
    public void onDifficultyIncreaseEvent(DifficultyIncreaseEvent event) {
        blocks.clear();
        blocks.addAll(generateMaterialList());
    }

    /**********************************************************
     * Helper Methods
     **********************************************************/

    //populate blockNames map
    private static void generateBlockNames() {
        String blockName;

        //sort materials
        for (Material m : blocks) {
            blockName = formatBlockName(m.toString());
            blockNames.put(m, blockName);
        }
    }

    //generate a random material
    private static Material generateBlock() {
        return blocks.get(new Random().nextInt(blocks.size() - 1));
    }

    //generate material list from banned materials in config file
    private static List<Material> generateMaterialList() {
        Material[] values = Material.values();
        List<Material> materials = new ArrayList<>();

        for (Material m : values) {
            if (m.isSolid()) {
                materials.add(m);
            }
            for (String s : BlockHunters.BANNED_MATERIALS) {
                if (m.toString().contains(s)) {
                    materials.remove(m);
                }
            }
        }

        return materials;
    }

    //humanize the block names from the material list
    private static String formatBlockName(String name) {
        String[] split = name.split("_");
        StringBuilder formattedName = new StringBuilder();

        for (String s : split) {
            if (split.length == 1) {
                formattedName.append(s.charAt(0)).append(s.substring(1).toLowerCase());
                return formattedName.toString();
            }
            formattedName.append(s.charAt(0)).append(s.substring(1).toLowerCase()).append(" ");
        }

        return formattedName.toString().trim();
    }

    //check if player can be targeted (in dimension, non-hunter target) and set compass target
    private static void targetPlayer(Player p, Player t) {
        World targetWorld;

        if (p.getInventory().contains(compass) && BlockHunters.getGameState()) {
            targetWorld = t.getLocation().getWorld();
            if (targetWorld != null) {
                if (targetWorld.getEnvironment().equals(p.getWorld().getEnvironment()) && !hunters.contains(t)) {
                    compassTargets.put(p, t);
                    p.setCompassTarget(compassTargets.get(p).getLocation());
                }
                else {
                    p.sendMessage(ChatColor.DARK_RED + t.getDisplayName() + " is not in this dimension.");
                }
            }
        }
    }

    //give compass to player
    public static void getCompass(Player p) {
        if (!p.getInventory().contains(compass)) {
            if (p.getInventory().firstEmpty() != -1) {
                p.getInventory().addItem(compass);
                p.updateInventory();
                targetPlayer(p, players.get(0));
                p.sendMessage(ChatColor.DARK_PURPLE + "A compass has been given to you.");
                p.sendMessage(ChatColor.DARK_PURPLE + "Right-click to change targets.");
            } else {
                p.sendMessage(ChatColor.DARK_RED + "Could not give compass. Inventory is full.");
                p.sendMessage(ChatColor.DARK_PURPLE + "Use " + ChatColor.GOLD + "/bh compass " + ChatColor.DARK_PURPLE + " to get a new compass.");
            }
        }
    }

    //remove player from players map and add to hunters map
    private void setHunter(Player p) {

        //Remove player from player list at 0 lives and add them to the hunter list if more than 1 player remains.
        //Give new hunters a compass to track other players

        //remove player
        players.remove(p);
        lives.remove(p);
        currentSearchBlock.remove(p);
        foundBlock.remove(p);
        plugin.getServer().broadcastMessage(ChatColor.DARK_RED + p.getDisplayName() + " is out of lives!");

        if (players.size() == 1) {
            plugin.getServer().getPluginManager().callEvent(new ClassicStopEvent());
        }

        else {
            //add hunter
            hunters.add(p);
            getCompass(p);
            plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + p.getDisplayName() + " is now a Hunter.");
        }
    }

    //subtract life and set hunter if 0 lives
    private void subtractLife(Player p) {
        int l = lives.get(p) - 1;
        lives.put(p, l);
        if (l == 0) {
            plugin.getServer().getPluginManager().callEvent(new CreateHunterEvent(p));
        }
    }

    //add life
    private void addLife(Player p) {
        int l = lives.get(p) + 1;
        lives.put(p, l);
    }

    /**********************************************************
     * Material Removal Methods - Used for difficulty++
     **********************************************************/

    public static void removeRareMaterials() {
        BANNED_MATERIALS.removeAll(RARE_MATERIALS);
        BANNED_MATERIALS.removeAll(NETHER_MATERIALS);
    }

    public static void removeEpicMaterials() {
        BANNED_MATERIALS.removeAll(EPIC_MATERIALS);
        BANNED_MATERIALS.removeAll(OVERWORLD_MATERIALS);
        BANNED_MATERIALS.removeAll(END_MATERIALS);
    }

    public static void removeLegendaryMaterials() {
        BANNED_MATERIALS.removeAll(LEGENDARY_MATERIALS);
    }
}