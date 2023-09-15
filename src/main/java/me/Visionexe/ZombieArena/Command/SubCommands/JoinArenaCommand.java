package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.ZombieArena;
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
    public void perform(Player player, String[] args) {
        GameHandler gameHandler = ZombieArena.getInstance().getGameHandler();
        String arenaName;

        if (args.length == 2) {
            arenaName = args[1].toLowerCase();
            // Check if arena is valid
            if (gameHandler.getArenaHandler().isArenaValid(arenaName)) {
                gameHandler.addPlayer(player, arenaName);
            } else {
                player.sendMessage("No arena exists with that name!");
            }
        } else {
            player.sendMessage("Please specify an Arena Name");
        }
    }
}
