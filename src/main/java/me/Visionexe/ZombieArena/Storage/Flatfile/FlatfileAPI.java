package me.Visionexe.ZombieArena.Storage.Flatfile;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

/**
 * <p>Handles creation and storage of YAML flatfiles.<br />
 * This API can NOT be used in static reference, it must be instantiated ONCE.<br />
 * I recommend instantiating the flatfile API in your main class inside the onEnable method.</p>
 */
public class FlatfileAPI {

    private Plugin plugin;

    private HashMap<String, Configuration> configurations;

    /**
     * <p>Instantiates a new Flatfile handler.<br />
     * I recommend instantiating it once inside your onEnable method in your plugin's main class.</p>
     * @param plugin Instance of your main class
     */
    public FlatfileAPI(Plugin plugin) {
        this.plugin = plugin;
        this.configurations = new HashMap<String, Configuration>();
    }

    /**
     * <p>Add, register, create and setup new configurations for your plugin.</p>
     * @param configs
     */
    public void add(Config... configs) {
        for (Config config : configs) {
            this.configurations.put(config.label, new Configuration(this.plugin, config.label, config.resourcePath, config.resourceName, config.filePath, config.fileName));
        }
    }

    /**
     * Saves all configurations
     */
    public void save() {
        configurations.keySet().forEach(this::save);
    }

    /**
     * Saves the specified configurations/s
     * @param labels The unique identifier for your configurations
     */
    public void save(String... labels) {
        for (String label : labels) {
            if (this.configurations.containsKey(label)) this.configurations.get(label).save();
        }
    }

    /**
     * Saves the specified configurations/s
     * @param configs The Config files
     */
    public void save(Config... configs) {
        for (Config config : configs) {
            save(config.label);
        }
    }

    /**
     * Loads all configurations
     */
    public void load() {
        configurations.keySet().forEach(this::load);
    }

    /**
     * Loads the specified configurations/s
     * @param labels The unique identifier for your configurations
     */
    public void load(String... labels) {
        for (String label : labels) {
            if (this.configurations.containsKey(label)) this.configurations.get(label).load();
        }
    }

    /**
     * Loads the specified configurations/s
     * @param configs The Config files
     */
    public void load(Config... configs) {
        for (Config config : configs) {
            load(config.label);
        }
    }

    /**
     * Get the corresponding configurations file.
     * @param label The unique identifier of your configurations
     * @return FileConfiguration - Used to retrieve data from the configs
     */
    public FileConfiguration get(String label) {
        return this.configurations.get(label).getFileConfiguration();
    }

    /**
     * Get the corresponding configurations file.
     * @param config The Config file
     * @return FileConfiguration - Used to retrieve data from the configs
     */
    public FileConfiguration get(Config config) {
        return get(config.label);
    }

    public boolean contains(String label) {
        return this.configurations.containsKey(label);
    }

}
