package me.Visionexe.ZombieArena.Command.SubCommands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.session.SessionOwner;
import com.sk89q.worldedit.world.World;
import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Game.Arena;
import org.bukkit.ChatColor;
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
    public void perform(Player player, String[] args) {
        /*
        Using Arena class, create a new arena based off the selection given a name
         */

        if (args.length == 2) {
            SessionManager manager = WorldEdit.getInstance().getSessionManager();
            LocalSession localSession = manager.get(BukkitAdapter.adapt(player));
            CuboidRegion region;
            World selectionWorld = localSession.getSelectionWorld();
            try {
                if (selectionWorld == null) throw new IncompleteRegionException();
                region = (CuboidRegion) localSession.getSelection(selectionWorld);
            } catch (IncompleteRegionException exception) {
                player.sendMessage(ChatColor.DARK_RED + "Please make a region selection first.");
                return;
            }
            // TODO: Get all file names in arenas/ folder and compare to args[1], if matches, deny arena creation and prompt to specify different name
            new Arena(args[1], region.getPos1(), region.getPos2());
            player.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + "successfully created.");

            // TODO: Possibly get the center of the square and set the default player spawn to the center
        } else {
            player.sendMessage(ChatColor.DARK_RED + "Please provide an arena name.");
        }
    }
}
