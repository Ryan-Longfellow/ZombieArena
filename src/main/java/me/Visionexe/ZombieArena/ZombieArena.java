package me.Visionexe.ZombieArena;

import fr.mrmicky.fastboard.FastBoard;
import me.Visionexe.ZombieArena.Command.CommandHandler;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.Listener.MobListener;
import me.Visionexe.ZombieArena.Listener.PlayerListener;
import me.Visionexe.ZombieArena.Storage.DatabaseConnection;
import me.Visionexe.ZombieArena.Storage.Flatfile.FileManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ZombieArena extends JavaPlugin {
    private static ZombieArena instance;
    private FileManager fileManager;
    private GameHandler gameHandler;
    private Economy economy;
    public Map<UUID, FastBoard> boards = new HashMap<>();
    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Enabling " + this.getDescription().getName() + " " + this.getDescription().getVersion());
        registerConfigs();
        prepareDatabase();

        gameHandler = new GameHandler();

        registerEconomy();
        registerEvents();
        registerCommands();

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
        gameHandler.stop();
        this.fileManager = new FileManager(this);
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

            Optional<PreparedStatement> psOptional = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `players` (uuid varchar(190) PRIMARY KEY NOT NULL, level INT NOT NULL, experience INT NOT NULL, prestige INT NOT NULL, games_played INT NOT NULL, games_won INT NOT NULL, total_kills INT NOT NULL);");
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

        for (Player player : gameHandler.getPlayers()) {
            if (gameHandler.getPlayers().contains(player)) {
                board.updateLines(
                        ChatColor.AQUA + "" + ChatColor.BOLD + "Info",
                        ChatColor.GRAY + "  Name: " + ChatColor.WHITE + board.getPlayer().getName(),
                        ChatColor.GOLD + "  Level: " + ChatColor.WHITE + playerWrapper.getLevel(),
                        ChatColor.DARK_GREEN + "  Exp: " + ChatColor.WHITE + playerWrapper.getExperience() + " / " + playerWrapper.getExperienceForNextLevel(),

                        " ", // White space to separate Info and Stats

                        ChatColor.YELLOW + "" + ChatColor.BOLD + "Stats",
                        ChatColor.GREEN + "  Money: " + ChatColor.WHITE + this.getEconomy().getBalance(board.getPlayer()),
                        ChatColor.RED + "  Total Kills: " + ChatColor.WHITE + playerWrapper.getTotalKills(),

                        " ", // White space
                        ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Game",
                        ChatColor.LIGHT_PURPLE + "  Time In Game: " + gameHandler.getWaveHandler().getGameLength(),
                        ChatColor.LIGHT_PURPLE + "  Wave: " + ChatColor.WHITE +  gameHandler.getWaveHandler().getWave(), // Wave number
                        ChatColor.LIGHT_PURPLE + "  Mobs Remaining: " + ChatColor.WHITE + gameHandler.getWaveHandler().getRemainingZombies() // Mobs Remaining
                );
                return;
            }
        }
        board.updateLines(
                ChatColor.AQUA + "" + ChatColor.BOLD + "Info",
                ChatColor.GRAY + "  Name: " + ChatColor.WHITE + board.getPlayer().getName(),
                ChatColor.GOLD + "  Level: " + ChatColor.WHITE + playerWrapper.getLevel(),
                ChatColor.DARK_GREEN + "  Exp: " + ChatColor.WHITE + playerWrapper.getExperience() + " / " + playerWrapper.getExperienceForNextLevel(),

                " ", // White space to separate Info and Stats

                ChatColor.YELLOW + "" + ChatColor.BOLD + "Stats",
                ChatColor.GREEN + "  Money: " + ChatColor.WHITE + this.getEconomy().getBalance(board.getPlayer()),
                ChatColor.RED + "  Total Kills: " + ChatColor.WHITE + playerWrapper.getTotalKills()
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
    public GameHandler getGameHandler() { return gameHandler; }

    public static ZombieArena getInstance() {
        return instance;
    }

    private int tick;
    private void onTick() { tick++; }
}
