package me.Visionexe.ZombieArena.Storage.Flatfile;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// from https://gist.github.com/WesJD/bc4ece6717f13b35272de7094e72676c
public class FileManager {
    /*
    Recreate the existing file manager, so I can more understand exactly how all files are managed and how to obtain their information
     */
    private final Plugin plugin;
    private final List<ManagedFile> managedFiles = new ArrayList<>();

    public FileManager(Plugin plugin) {
        this.plugin = plugin;
        plugin.getDataFolder().mkdirs();
    }

    public void register(String name) throws IOException {
//        Validate.isTrue(get(name).isPresent(), "A ManagedFile with the name of " + name + " already exists");
        managedFiles.add(new ManagedFile(plugin.getDataFolder(), name));
    }

    public void reload(String name) throws IOException {
        get(name).get().reload();
    }

    public void save(String name) throws IOException {
        get(name).get().save();
    }

    public Optional<ManagedFile> get(String name) {
        return managedFiles.stream()
                .filter(managedFile -> managedFile.name.equals(name))
                .findFirst();
    }

    public static class ManagedFile {

        private final String name;
        private final File file;
        private YamlConfiguration configuration;

        ManagedFile(File dataFolder, String name) throws IOException {
            this.name = name;
            this.file = new File(dataFolder, name + ".yml");
            if(!file.exists()) file.createNewFile();
            this.configuration = YamlConfiguration.loadConfiguration(file);
        }

        public void reload() throws IOException {
            save();
            configuration = YamlConfiguration.loadConfiguration(file);
        }

        public void save() throws IOException {
            configuration.save(file);
        }

        public String getName() {
            return name;
        }

        public File getFile() {
            return file;
        }

        public YamlConfiguration getConfiguration() {
            return configuration;
        }
    }
}
