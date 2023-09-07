package me.Visionexe.ZombieArena.Game;

import me.Visionexe.ZombieArena.Storage.Flatfile.FileManager;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArenaHandler {
    private List<String> arenas = new ArrayList<>();

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
        FileManager fileManager = ZombieArena.getInstance().getFileManager();
        FileConfiguration arenasFile = fileManager.get("arenas").get().getConfiguration();

        Set<String> setOfArenas = arenasFile.getKeys(false);
        arenas.addAll(setOfArenas);

        /*
        TODO: Get all information for each arena and store it
        Use the information stored to get player spawn, mob spawns or any other information
        Create functions to get information stored to be accessed in the GameHandler and WaveHandler
        GameHandler will use arena information when adding a player to the game
        WaveHandler will use arena information when spawning mobs in the game
         */

    }
}
