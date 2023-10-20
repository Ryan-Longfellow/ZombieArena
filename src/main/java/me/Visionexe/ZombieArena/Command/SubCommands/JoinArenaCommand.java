package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.GUI.Blacksmith.Blacksmith;
import me.Visionexe.ZombieArena.GUI.Game.Join;
import me.Visionexe.ZombieArena.GUI.Row;
import me.Visionexe.ZombieArena.Game.ArenaHandler;
import me.Visionexe.ZombieArena.Game.GameDifficulty;
import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.Utils.Check;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.ChatColor;
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
            if (ZombieArena.getInstance().getPlayersInGame().contains(player)) {
                player.sendMessage("You are already in a game");
                return;
            }
            Join gui = new Join(ChatColor.translateAlternateColorCodes('&', "&aArenas"), Row.SIX);
            gui.open(player);
        } else {
            commandSender.sendMessage("Only players are allowed to use this command!");
        }
    }
}
