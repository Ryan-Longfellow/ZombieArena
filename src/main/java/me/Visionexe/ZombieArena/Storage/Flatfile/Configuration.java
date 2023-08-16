package me.Visionexe.ZombieArena.Storage.Flatfile;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;

class Configuration {

    protected String label;
    protected String resourcePath;
    protected String resourceName;
    protected String filePath;
    protected String fileName;

    protected File file;
    protected FileConfiguration config;

    protected Plugin plugin;

    /**
     * @param plugin
     * @param label        MUST BE UNIQUE. This is just an identifier for yourself. It has no practical use, besides for
     *                     you to know which configurations you are working with.
     * @param resourcePath
     * @param filePath
     * @param fileName
     */
    protected Configuration(Plugin plugin, String label, String resourcePath, String resourceName, String filePath, String fileName) {
        this.plugin = plugin;

        this.label = label;
        this.resourcePath = resourcePath;
        this.resourceName = resourceName;
        this.filePath = filePath;
        this.fileName = fileName;

        this.createFile();
        this.checkFile();
        this.createConfiguration();
    }

    protected void createFile() {
        file = new File(plugin.getDataFolder() + ((filePath.length() == 0 || filePath == null) ? "" : "/" + filePath), fileName);
    }

    protected void checkFile() {
        if (!file.exists()) {

            file.getParentFile().mkdirs();
//			if (resourceName == null)
//				copy(SkryptCoreAPI.getInstance().getResource("blank.yml"), file);
//			else
            copy(plugin.getResource(((resourcePath == null || resourcePath.length() == 0) ? "" : resourcePath + "/") + resourceName), file);
        }
    }

    protected void createConfiguration() {
        config = new YamlConfiguration();
        load();
    }


    /**
     * Saves the file into the plugin folder.
     *
     * @since 1.0.0
     */
    public void save() {
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Loads the file from the plugin folder.
     *
     * @since 1.0.0
     */
    public void load() {
        try {
            config.load(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Copies the content from the default config in the jar into it's plugin location.
     *
     * @param in   Input stream of the default config
     * @param file File destination to copy the content to
     *
     * @since 1.0.0
     */
    private void copy(InputStream in, File file) {
        try (OutputStream out = new FileOutputStream(file)) {
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public FileConfiguration getFileConfiguration() {
        return config;
    }

}
