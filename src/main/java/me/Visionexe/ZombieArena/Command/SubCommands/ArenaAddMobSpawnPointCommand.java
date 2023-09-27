package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Storage.Flatfile.FileManager;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArenaAddMobSpawnPointCommand extends SubCommand {
    @Override
    public String getName() {
        return "addmobspawn";
    }

    @Override
    public String getDescription() {
        return "Will add a mob spawn to the arena";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena addmobspawn <arena name>";
    }

    @Override
    public String getPermission() {
        return "zombiearena.admin";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            // Get player location
            Location playerLocation = player.getLocation();

            if (args.length == 2) {
                // Get arena name from argument
                String arenaName = args[1];

                FileManager fileManager = ZombieArena.getInstance().getFileManager();
                FileConfiguration arenas = fileManager.get("arenas").get().getConfiguration();

                // Get mob location list
                List<String> mobSpawns = (List<String>) arenas.getList(arenaName + ".MobSpawns", new ArrayList<String>());
                // Stores the value in the format "X,Y,Z" so it is easily obtained later when spawning mobs
                String spawnToAdd = playerLocation.getX() + "," + playerLocation.getY() + "," + playerLocation.getZ();
                mobSpawns.add(spawnToAdd);
                player.sendMessage("Location added!");
                arenas.set(arenaName + ".MobSpawns", mobSpawns);
                try {
                    fileManager.save("arenas");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                player.sendMessage("Please specify an Arena Name!");
            }
        } else {
            commandSender.sendMessage("Only players are allowed to use this command!");
        }
    }
}
