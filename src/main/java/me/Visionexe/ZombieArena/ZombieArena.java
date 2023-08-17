package me.Visionexe.ZombieArena;

import com.sk89q.worldedit.WorldEdit;
import me.Visionexe.ZombieArena.Command.CommandHandler;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.Listener.MobListener;
import me.Visionexe.ZombieArena.Listener.PlayerListener;
import me.Visionexe.ZombieArena.Storage.DatabaseConnection;
import me.Visionexe.ZombieArena.Storage.Flatfile.Config;
import me.Visionexe.ZombieArena.Storage.Flatfile.FlatfileAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class ZombieArena extends JavaPlugin {
    public static final String FILE_SETTINGS = "settings";
    public static final String FILE_MESSAGES = "messages";

    private static ZombieArena instance;

    private FlatfileAPI flatfile;
    private Economy economy;
    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        getLogger().info("Enabling " + this.getDescription().getName() + " " + this.getDescription().getVersion());
        registerConfigs();
        prepareDatabase();
        registerEconomy();
        registerEvents();
        registerCommands();
    }
    @Override
    public void onDisable() {
        super.onDisable();
        PlayerWrapper.saveAll();
        getLogger().info("Disabling " + this.getDescription().getName() + " " + this.getDescription().getVersion());
    }

    /*
    Register configs, will create defaults if none are detected
     */
    private void registerConfigs() {
        Bukkit.getConsoleSender().sendMessage("Registering config(s)...");
        this.flatfile = new FlatfileAPI(this);
        this.flatfile.add(new Config(FILE_SETTINGS, "", "settings.yml", "", "settings.conf"));
        this.flatfile.add(new Config(FILE_MESSAGES, "", "messages.yml", "", "messages.lang"));
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

    public FlatfileAPI getFlatfile() {
        return flatfile;
    }

    public static ZombieArena getInstance() {
        return instance;
    }
}
