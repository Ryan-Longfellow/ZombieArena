package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Storage.Flatfile.FlatfileAPI;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
    public void perform(Player player, String[] args) {
        /*
        Confirm player is inside the region of the Arena from the Arena class, if so add the player spawn into the yml file of the arena
         */
        // Get position of player
        Location playerLocation = player.getLocation();
        // Get pos1 and pos2 of Arena from the Arena File
        FlatfileAPI flatfile = ZombieArena.getInstance().getFlatfile();
        FileConfiguration fileConfig = flatfile.get("arena." + args[1]);
        Location pos1 = new Location(player.getWorld(), fileConfig.getDouble("Position1.x"), fileConfig.getDouble("Position1.y"), fileConfig.getDouble("Position1.z"));
        Location pos2 = new Location(player.getWorld(), fileConfig.getDouble("Position2.x"), fileConfig.getDouble("Position2.y"), fileConfig.getDouble("Position2.z"));
        if (args.length == 2) {
            // Perform a check to see if position of player is inside pos1 and pos2
            // If so, set spawn; If not, prompt player to stand inside Arena
            double pos1X = pos1.getX();
            double pos1Z = pos1.getZ();
            double pos2X = pos2.getX();
            double pos2Z = pos2.getZ();
            double pX = playerLocation.getX();
            if((pos1X < pX && pX < pos2X || pos1X > pX && pX > pos2X && (pos1Z < pX && pX < pos2Z) || pos1Z > pX && pX > pos2Z)) {
                fileConfig.set("PlayerSpawn.x", playerLocation.getX());
                fileConfig.set("PlayerSpawn.y", playerLocation.getY());
                fileConfig.set("PlayerSpawn.z", playerLocation.getZ());
                flatfile.save();
                player.sendMessage(ChatColor.GREEN + "Player Spawn Successfully Set, " +
                        "X: " + playerLocation.getX() +
                        "Y: " + playerLocation.getY() +
                        "Z: " + playerLocation.getZ());
            } else {
                player.sendMessage(ChatColor.DARK_RED + "Please stand inside the Arena zone");
                player.sendMessage(ChatColor.DARK_RED + "Current arena coords: " + pos1 + pos2);
                player.sendMessage(ChatColor.DARK_RED + "Current location: " + playerLocation);
            }
        } else {
            player.sendMessage(ChatColor.DARK_RED + "Please provide an arena name.");
        }
    }
}
