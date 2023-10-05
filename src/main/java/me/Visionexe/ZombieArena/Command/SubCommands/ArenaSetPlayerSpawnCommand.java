package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Game.ArenaHandler;
import me.Visionexe.ZombieArena.Storage.Flatfile.FileManager;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ArenaSetPlayerSpawnCommand extends SubCommand {
    @Override
    public String getName() {
        return "setplayerspawn";
    }

    @Override
    public String getDescription() {
        return "Sets the spawn of the player in a given arena to the current position";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena setplayerspawn <arena name>";
    }

    @Override
    public String getPermission() {
        return "zombiearena.admin";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 2) {
                // Get arena name and confirm the arena is valid
                String arenaName = args[1];
                if (!(ArenaHandler.isArenaValid(arenaName))) {
                    player.sendMessage("Please enter a valid arena name");
                }
                // Get position of player
                Location playerLocation = player.getLocation();

                FileManager fileManager = ZombieArena.getInstance().getFileManager();
                FileConfiguration arenas = ZombieArena.getInstance().getArenasFile();

                // Get pos1 and pos2 of Arena from the Arena File
                Location pos1 = new Location(player.getWorld(), arenas.getDouble(arenaName + ".Position1.x"), arenas.getDouble(arenaName + ".Position1.y"), arenas.getDouble(arenaName + ".Position1.z"));
                Location pos2 = new Location(player.getWorld(), arenas.getDouble(arenaName + ".Position2.x"), arenas.getDouble(arenaName + ".Position2.y"), arenas.getDouble(arenaName + ".Position2.z"));

                // Perform a check to see if position of player is inside pos1 and pos2
                // If so, set spawn; If not, prompt player to stand inside Arena
                double pos1X = pos1.getX();
                double pos1Z = pos1.getZ();
                double pos2X = pos2.getX();
                double pos2Z = pos2.getZ();
                double pX = playerLocation.getX();
                if ((pos1X < pX && pX < pos2X || pos1X > pX && pX > pos2X && (pos1Z < pX && pX < pos2Z) || pos1Z > pX && pX > pos2Z)) {
                    arenas.set(arenaName + ".PlayerSpawn.x", playerLocation.getX());
                    arenas.set(arenaName + ".PlayerSpawn.y", playerLocation.getY());
                    arenas.set(arenaName + ".PlayerSpawn.z", playerLocation.getZ());
                    try {
                        fileManager.save("arenas");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    player.sendMessage("Player spawn for " + arenaName + " has been set " +
                            "[X " + playerLocation.getX() +
                            ",Y " + playerLocation.getY() +
                            ",Z " + playerLocation.getZ() + "]");
                } else {
                    player.sendMessage("Please stand inside arena " + arenaName);
                    player.sendMessage("Current arena coords: " +
                            "[" + pos1.getX() + ", " + pos1.getY() + ", " + pos1.getZ() + "] and " +
                            "[" + pos2.getX() + ", " + pos2.getY() + ", " + pos2.getZ() + "]");
                    player.sendMessage("Current location: " + "[" + playerLocation.getX() + ", " + playerLocation.getY() + ", " + playerLocation.getZ() + "]");
                }
            } else {
                player.sendMessage("Please follow the following syntax: " + getSyntax());
            }
        } else {
            commandSender.sendMessage("Only players are allowed to use this command!");
        }

    }
}
