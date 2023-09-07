package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Event.GameStartCause;
import me.Visionexe.ZombieArena.Event.GameStartEvent;
import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StartGameCommand extends SubCommand {
    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Starts game";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena start";
    }

    @Override
    public String getPermission() {
        return "zombiearena.admin";
    }

    @Override
    public void perform(Player player, String[] args) {
        GameHandler gameHandler = ZombieArena.getInstance().getGameHandler();
        gameHandler.start();
        GameStartEvent event = new GameStartEvent(GameStartCause.FORCE);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
}
