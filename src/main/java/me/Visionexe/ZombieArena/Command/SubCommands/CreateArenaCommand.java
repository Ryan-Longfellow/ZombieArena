package me.Visionexe.ZombieArena.Command.SubCommands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
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

        TODO: CURRENTLY THROWING EXCEPTION DUE TO BEING UNABLE TO CAST PLAYER TO ACTOR
        TODO: NEED TO FIGURE OUT A WAY TO CREATE AND ACTOR OR SESSIONMANAGER FROM BUKKIT PLAYER CLASS
         */
        if (args.length == 2) {
            SessionManager manager = WorldEdit.getInstance().getSessionManager();
            LocalSession localSession = manager.get((Actor) player);
            CuboidRegion region;
            World selectionWorld = localSession.getSelectionWorld();
            try {
                if (selectionWorld == null) throw new IncompleteRegionException();
                region = (CuboidRegion) localSession.getSelection(selectionWorld);
            } catch (IncompleteRegionException exception) {
                player.sendMessage(ChatColor.DARK_RED + "Please make a region selection first.");
                return;
            }
            new Arena(args[1], region.getPos1(), region.getPos2());
        } else {
            player.sendMessage(ChatColor.DARK_RED + "Please provide an arena name.");
        }
    }
}
