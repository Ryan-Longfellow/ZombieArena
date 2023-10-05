package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.Utils.Check;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWaveCommand extends SubCommand {
    @Override
    public String getName() {
        return "setwave";
    }

    @Override
    public String getDescription() {
        return "Sets the wave of the given arena to the given number";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena setwave <arena name> <wave>";
    }

    @Override
    public String getPermission() {
        return "zombiearena.admin";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player player) {
            GameHandler gameHandler = ZombieArena.getInstance().getGamePlayerIn(player);
            if (args.length == 3) {
                if (gameHandler.getPlayerStats(player).getArenaName().equals(args[1])) {
                    if (!(Check.isInteger(args[2]))) {
                        commandSender.sendMessage("Please enter a valid wave integer (1-50)");
                        return;
                    }
                    gameHandler.getWaveHandler().setWave(Integer.parseInt(args[2]));
                } else {
                    player.sendMessage("You are not in the arena you are trying to set the wave for");
                }
            } else {
                commandSender.sendMessage("Please follow the following syntax: " + getSyntax());
            }
        } else {
            commandSender.sendMessage("Only players are allowed to use this command!");
        }
    }
}
