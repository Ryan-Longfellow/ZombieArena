package me.Visionexe.ZombieArena;

import fr.mrmicky.fastboard.FastBoard;
import me.Visionexe.ZombieArena.Command.CommandHandler;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.Game.GameDifficulty;
import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.Listener.MobListener;
import me.Visionexe.ZombieArena.Listener.PlayerListener;
import me.Visionexe.ZombieArena.Storage.DatabaseConnection;
import me.Visionexe.ZombieArena.Storage.Flatfile.FileManager;
import me.Visionexe.ZombieArena.Utils.ValueFormat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ZombieArena extends JavaPlugin {
    private static ZombieArena instance;
    private FileManager fileManager;
//    private GameHandler gameHandler;
    private Economy economy;
    public Map<UUID, FastBoard> boards = new HashMap<>();

    // Strings stored as "ArenaName_Difficulty"
    public Map<String, GameHandler> games = new HashMap<>();
    /*
    TODO: Add multi game functionality
    Do this by using the list of games above
    - Add difficulty to /za join so it is /za join <arena name> <difficulty>
    - When player runs command, have it create a new game and add to list
    - More functions will be needed to access specific games or all games
    - - Example: with scoreboard, will need to access all current games that are running and display the information for that game
    - Difficulty also needs to be passed into GameHandler to use in WaveHandler for increased mob stats
    - Information may need to be accessed differently as some things used ZombieArena.getInstance().getGameHandler...
     */
    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Enabling " + this.getDescription().getName() + " " + this.getDescription().getVersion());
        registerConfigs();
        prepareDatabase();
        registerEconomy();
        registerEvents();
        registerCommands();

//        gameHandler = new GameHandler();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                onTick();
                for (FastBoard board : boards.values()) {
                    updateBoard(board);
                }
            }
        }, 1L, 1L);
    }

    @Override
    public void onDisable() {
        PlayerWrapper.saveAll();
//        gameHandler.stop();
        try {
            fileManager.save("config");
            fileManager.save("arenas");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info("Disabling " + this.getDescription().getName() + " " + this.getDescription().getVersion());
    }

    /*
    Register configs, will create defaults if none are detected
     */
    private void registerConfigs() {
        Bukkit.getConsoleSender().sendMessage("Registering config(s)...");

        this.fileManager = new FileManager(this);
        try {
            fileManager.register("config");
            fileManager.register("arenas");

            fileManager.reload("config");
            fileManager.reload("arenas");

            fileManager.save("config");
            fileManager.save("arenas");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage("Config(s) registered!");
    }

    /*
    Setup database and create defaults
     */
    private void prepareDatabase() {
        Bukkit.getConsoleSender().sendMessage("Setting up database(s)...");
        try {
            DatabaseConnection connection = new DatabaseConnection();

            Optional<PreparedStatement> psOptional = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `players` (uuid varchar(190) PRIMARY KEY NOT NULL, " +
                    "level INT NOT NULL, " +
                    "experience INT NOT NULL, " +
                    "prestige INT NOT NULL, " +
                    "games_played INT NOT NULL, " +
                    "games_won INT NOT NULL, " +
                    "total_kills INT NOT NULL, " +
                    "zombie_kills INT NOT NULL, " +
                    "skeleton_kills INT NOT NULL, " +
                    "spider_kills INT NOT NULL, " +
                    "piglin_brute_kills INT NOT NULL, " +
                    "zoglin_kills INT NOT NULL, " +
                    "blaze_kills INT NOT NULL, " +
                    "wither_skeleton_kills INT NOT NULL, " +
                    "total_boss_kills INT NOT NULL, " +
                    "wave_10_boss_kills INT NOT NULL, " +
                    "wave_20_boss_kills INT NOT NULL, " +
                    "wave_30_boss_kills INT NOT NULL, " +
                    "wave_40_boss_kills INT NOT NULL, " +
                    "wave_50_boss_kills INT NOT NULL, " +
                    "total_boss_damage INT NOT NULL, " +
                    "wave_10_boss_damage INT NOT NULL, " +
                    "wave_20_boss_damage INT NOT NULL, " +
                    "wave_30_boss_damage INT NOT NULL, " +
                    "wave_40_boss_damage INT NOT NULL, " +
                    "wave_50_boss_damage INT NOT NULL" +
                    ");");
            if (psOptional.isEmpty()) {
                Bukkit.getConsoleSender().sendMessage("Database(s) could not be set up!");
                this.getPluginLoader().disablePlugin(this);
                return;
            }

            PreparedStatement ps = psOptional.get();

            connection.update(ps);

            ps.close();
            connection.close();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("Database(s) could not be set up!");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        Bukkit.getConsoleSender().sendMessage("Database(s) set up!");
    }

    /*
    Register events/listeners
     */
    private void registerEvents() {
        Bukkit.getConsoleSender().sendMessage("Setting up event(s)...");
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new MobListener(), this);
        Bukkit.getConsoleSender().sendMessage("Event(s) set up!");
    }

    /*
    Register commands
     */
    private void registerCommands() {
        Bukkit.getConsoleSender().sendMessage("Registering command(s)...");
        this.getCommand("zombiearena").setExecutor(new CommandHandler());
        Bukkit.getConsoleSender().sendMessage("Command(s) set up!");
    }

    /*
    Registers economy support
     */
    private void registerEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        this.economy = rsp.getProvider();
    }

    private void updateBoard(FastBoard board) {
        /*
        Scoreboard Example
                ZombieArena
        Info
            Name: <player name>
            Level: <level>
            Exp: <current exp> / <exp to next level>

        Stats
            Money: <money>
            Total Kills: <kills>
         */

        PlayerWrapper playerWrapper = PlayerWrapper.get(board.getPlayer());

        board.updateTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "ZombieArena");
//        if (gameHandler != null) {
//            for (Player player : gameHandler.getPlayers()) {
//                if (gameHandler.getPlayers().contains(player)) {
//                    board.updateLines(
//                            ChatColor.AQUA + "" + ChatColor.BOLD + "Info",
//                            ChatColor.GRAY + "  Name" + ChatColor.WHITE + ": " + board.getPlayer().getName(),
//                            ChatColor.GOLD + "  Level" + ChatColor.WHITE + ": " + playerWrapper.getLevel(),
//                            ChatColor.DARK_GREEN + "  Exp" + ChatColor.WHITE + ": " + playerWrapper.getExperience() + " / " + playerWrapper.getExperienceForNextLevel(),
//
//                            " ", // White space to separate Info and Stats
//
//                            ChatColor.YELLOW + "" + ChatColor.BOLD + "Stats",
//                            ChatColor.GREEN + "  Money" + ChatColor.WHITE + ": " + ValueFormat.format((long) this.getEconomy().getBalance(board.getPlayer())),
//                            ChatColor.RED + "  Total Kills" + ": " + ChatColor.WHITE + playerWrapper.getTotalKills(),
//
//                            " ", // White space
//                            ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Game",
//                            ChatColor.LIGHT_PURPLE + "  Time In Game" + ChatColor.WHITE + ": " + gameHandler.getWaveHandler().getGameLength(),
//                            ChatColor.LIGHT_PURPLE + "  Wave" + ChatColor.WHITE + ": " + gameHandler.getWaveHandler().getWave() + " / " + gameHandler.getWaveHandler().getMaxWave(), // Wave number
//                            ChatColor.LIGHT_PURPLE + "  Mobs Remaining" + ChatColor.WHITE + ": " + gameHandler.getWaveHandler().getRemainingZombies(), // Mobs Remaining
//                            ChatColor.LIGHT_PURPLE + "  Players" + ChatColor.WHITE + ": " + gameHandler.getPlayers().size() + " / " + gameHandler.getMaxPlayers()
//                    );
//                    return;
//                }
//            }
//        }

        board.updateLines(
                ChatColor.AQUA + "" + ChatColor.BOLD + "Info",
                ChatColor.GRAY + "  Name" + ChatColor.WHITE + ": " + board.getPlayer().getName(),
                ChatColor.GOLD + "  Level" + ChatColor.WHITE + ": " + playerWrapper.getLevel(),
                ChatColor.DARK_GREEN + "  Exp" + ChatColor.WHITE + ": " + playerWrapper.getExperience() + " / " + playerWrapper.getExperienceForNextLevel(),

                " ", // White space to separate Info and Stats

                ChatColor.YELLOW + "" + ChatColor.BOLD + "Stats",
                ChatColor.GREEN + "  Money" + ChatColor.WHITE + ": " + ValueFormat.format((long) this.getEconomy().getBalance(board.getPlayer())),
                ChatColor.RED + "  Total Kills" + ChatColor.WHITE + ": " + playerWrapper.getTotalKills()
        );
        boards.put(board.getPlayer().getUniqueId(), board);
    }

    /*
    Checks if an economy plugin is found
     */
    public boolean isEconomyEnabled() {
        return economy != null;
    }

    /*
    Gets economy API
     */
    public Economy getEconomy() {
        return economy;
    }

    public FileManager getFileManager() {
        return fileManager;
    }
    @NotNull
    public YamlConfiguration getConfigFile() {
        return getFileManager().get("config").get().getConfiguration();
    }
    @NotNull
    public YamlConfiguration getArenasFile() {
        return getFileManager().get("arenas").get().getConfiguration();
    }
//    public GameHandler getGameHandler() { return gameHandler; }

    public static ZombieArena getInstance() {
        return instance;
    }

    private int tick;
    private void onTick() { tick++; }

    /*
    Multi game functionality functions
     */
    public Map<String, GameHandler> getGames() { return this.games; }
    public void addGame(String arenaName, GameDifficulty gameDifficulty) {
        GameHandler gameHandler = new GameHandler(gameDifficulty);
        getGames().put(arenaName + "_" + gameDifficulty.toString(), gameHandler);
    }
    public List<Player> getPlayersInGame() {
        List<Player> players = new ArrayList<>();
        for (Map.Entry<String, GameHandler> game : getGames().entrySet()) {
            players.addAll(game.getValue().getPlayers());
        }
        return players;
    }
    public GameHandler getGamePlayerIn(Player player) {

        // TODO: Figure out why this does not work
        for (Map.Entry<String, GameHandler> game : getGames().entrySet()) {
            if (game.getValue().getPlayers().contains(player)) {
                return game.getValue();
            }
        }
        /*
        Games are stored by "ArenaName_Difficulty, GameHandler"

         */

//        Collection<GameHandler> games = getGames().values();
//        Iterator<GameHandler> gameHandlerIterator = games.iterator();
//        GameHandler gameHandler;
//        while(gameHandlerIterator.hasNext()) {
//            gameHandler = gameHandlerIterator.next();
//            Log.debug("Checking " + player.getName() + " in " + getGames().);
//            if (gameHandler.getPlayers().contains(player)) {
//                return gameHandler;
//            }
//        }
        return null;
    }
}
