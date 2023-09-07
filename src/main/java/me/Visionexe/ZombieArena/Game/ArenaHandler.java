package me.Visionexe.ZombieArena.Game;

import me.Visionexe.ZombieArena.Storage.Flatfile.FileManager;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArenaHandler {
    private List<String> arenas = new ArrayList<>();
    private FileManager fileManager = ZombieArena.getInstance().getFileManager();
    private FileConfiguration arenasFile = fileManager.get("arenas").get().getConfiguration();

    public ArenaHandler() {

    }

    public String getArena(String arenaName) {
        for (String arena : arenas) {
            if (arenas.contains(arenaName)) {
                return arena;
            }
        }
        return null;
    }

    public void loadArenas() {
        Set<String> setOfArenas = arenasFile.getKeys(false);
        arenas.addAll(setOfArenas);
    }

    public Location getPlayerSpawn(String arenaName) {
        World world = Bukkit.getWorld(arenasFile.getString(arenaName + ".World"));
        return new Location(
                world,
                arenasFile.getInt(arenaName + ".PlayerSpawn.x"),
                arenasFile.getInt(arenaName + ".PlayerSpawn.y"),
                arenasFile.getInt(arenaName + ".PlayerSpawn.z")
        );
    }

    public List<Location> getMobSpawns(String arenaName) {
        List<String> mobSpawnsString = arenasFile.getStringList(arenaName + ".MobSpawns");
        List<Location> mobSpawnsLocation = new ArrayList<>();

        for (String mobSpawn : mobSpawnsString) {
            String[] splitSpawn = mobSpawn.split(",");
            mobSpawnsLocation.add(new Location(
                    Bukkit.getWorld(arenasFile.getString(arenaName + ".World")),
                    Double.parseDouble(splitSpawn[0]),
                    Double.parseDouble(splitSpawn[1]),
                    Double.parseDouble(splitSpawn[2])
            ));
        }

        return mobSpawnsLocation;
    }
}
