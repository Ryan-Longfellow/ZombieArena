package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.entity.Player;

public class LeaveCommand extends SubCommand {
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Leaves the arena you are currently in.";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena leave";
    }

    @Override
    public String getPermission() {
        return "zombiearena.player";
    }

    @Override
    public void perform(Player player, String[] args) {
        GameHandler gameHandler = ZombieArena.getInstance().getGameHandler();
        if (gameHandler.getPlayers().contains(player)) {
            gameHandler.removePlayer(player);
        } else {
            player.sendMessage("You are not in a game.");
        }
    }
}
