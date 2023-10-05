package me.Visionexe.ZombieArena.Command.SubCommands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.world.World;
import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Game.Arena;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateArenaCommand extends SubCommand {
    @Override
    public String getName() {
        return "createarena";
    }

    @Override
    public String getDescription() {
        return "Create arena with WorldEdit selection.";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena createarena <name>";
    }

    @Override
    public String getPermission() {
        return "zombiearena.admin";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player player) {
            /*
            Using Arena class, create a new arena based off the selection given a name
             */

            if (args.length == 2) {
                // Gather WorldEdit selection
                SessionManager manager = WorldEdit.getInstance().getSessionManager();
                LocalSession localSession = manager.get(BukkitAdapter.adapt(player));
                CuboidRegion region;
                World selectionWorld = localSession.getSelectionWorld();
                try {
                    if (selectionWorld == null) throw new IncompleteRegionException();
                    region = (CuboidRegion) localSession.getSelection(selectionWorld);
                } catch (IncompleteRegionException exception) {
                    player.sendMessage("Please make a region selection first");
                    return;
                }
                // Confirm arena does not already exist with the same name
                if (ZombieArena.getInstance().getGameHandler().getArenaHandler().isArenaValid(args[1])) {
                    player.sendMessage("An arena with this name already exists");
                    return;
                }
                new Arena(args[1].toLowerCase(), player.getLocation(), region.getPos1(), region.getPos2());
                player.sendMessage("Arena " + args[1] + " successfully created.");
            } else {
                player.sendMessage("Please follow the following syntax: " + getSyntax());
            }
        } else {
            commandSender.sendMessage("Only players are allowed to use this command!");
        }
    }
}
