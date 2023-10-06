package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Game.ArenaHandler;
import me.Visionexe.ZombieArena.Game.GameDifficulty;
import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.Utils.Check;
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
            if (args.length == 3) {
                // Make sure player is not in a game
                if (ZombieArena.getInstance().getPlayersInGame().contains(player)) {
                    player.sendMessage("You are already in a game");
                    return;
                }

                String arenaName = args[1].toLowerCase();
                String difficulty = args[2].toUpperCase();
                GameDifficulty gameDifficulty;
                gameDifficulty = Check.getDifficulty(difficulty);
                // Check if arena is valid
                if (ArenaHandler.isArenaValid(arenaName)) {
                    if (ZombieArena.getInstance().getGames().containsKey(arenaName + "_" + gameDifficulty.toString())) {
                        ZombieArena.getInstance().getGames().get(arenaName + "_" + gameDifficulty).addPlayer(player, arenaName);
                    } else {
                        GameHandler gameHandler = new GameHandler(gameDifficulty);
                        ZombieArena.getInstance().addGame(arenaName + "_" + gameDifficulty, gameHandler);
                        gameHandler.addPlayer(player, arenaName);
                    }

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
