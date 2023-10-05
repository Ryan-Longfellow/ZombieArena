package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinArenaCommand extends SubCommand {
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Join arena";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena join <arena name>";
    }

    @Override
    public String getPermission() {
        return "zombiearena.player";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player player) {
            GameHandler gameHandler = ZombieArena.getInstance().getGameHandler();

            if (args.length == 2) {
                // Make sure player is not in a game
                if (gameHandler.getPlayers().contains(player)) {
                    player.sendMessage("You are already in a game");
                    return;
                }

                String arenaName = args[1].toLowerCase();
                // Check if arena is valid
                if (gameHandler.getArenaHandler().isArenaValid(arenaName)) {
                    gameHandler.addPlayer(player, arenaName);
                } else {
                    player.sendMessage("Arena name does not exist");
                }

            } else {
                player.sendMessage("Please follow the following syntax: " + getSyntax());
            }
        } else {
            commandSender.sendMessage("Only players are allowed to use this command!");
        }
    }
}
