package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
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
        // Since this has to get the current location where a player is standing, only allow players to execute
        if (commandSender instanceof Player player) {
            // Get player location
            Location playerLocation = player.getLocation();

            if (args.length == 2) {
                // Get arena name from argument
                String arenaName = args[1];
                // Check if the arena exists
                if (!(ZombieArena.getInstance().getGameHandler().getArenaHandler().isArenaValid(arenaName))) {
                    player.sendMessage("Please enter a valid arena name");
                }
                // Get the arenas file
                FileConfiguration arenas = ZombieArena.getInstance().getArenasFile();
                // Get mob location list
                List<String> mobSpawns = arenas.getStringList(arenaName + ".MobSpawns");
                // Stores the value in the format "X,Y,Z" so it is easily obtained later when spawning mobs
                String spawnToAdd = playerLocation.getX() + "," + playerLocation.getY() + "," + playerLocation.getZ();
                mobSpawns.add(spawnToAdd);
                player.sendMessage("Mob spawn [" + spawnToAdd + "] has been added");
                // Set the location in the file
                arenas.set(arenaName + ".MobSpawns", mobSpawns);
                try {
                    // Attempt to save the file
                    ZombieArena.getInstance().getFileManager().save("arenas");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                player.sendMessage("Please follow the following syntax: " + getSyntax());
            }
        } else {
            commandSender.sendMessage("Only players are allowed to use this command!");
        }
    }
}
