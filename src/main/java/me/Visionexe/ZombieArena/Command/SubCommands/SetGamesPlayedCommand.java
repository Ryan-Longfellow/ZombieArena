package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetGamesPlayedCommand extends SubCommand {
    @Override
    public String getName() {
        return "setgamesplayed";
    }

    @Override
    public String getDescription() {
        return "Set games played for the given player.";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena setgamesplayed <player> <gamesplayed>";
    }

    @Override
    public String getPermission() { return "zombiearena.admin"; }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 3) {
            Player getPlayer = Bukkit.getPlayer(args[1]);
            int gamesPlayed = Integer.parseInt(args[2]);

            if (getPlayer != null) {
                PlayerWrapper playerWrapper = PlayerWrapper.get(getPlayer);
                if (playerWrapper.setGamesPlayed(gamesPlayed)) {
                    if (getPlayer.equals(player)) {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your games played has been set to: " + ChatColor.YELLOW + gamesPlayed);
                    } else {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your games played has been set to: " + ChatColor.YELLOW + gamesPlayed);
                        player.sendMessage(
                                ChatColor.YELLOW + player.getName() + "'s" +
                                        ChatColor.GREEN + " games played has been set to " +
                                        ChatColor.YELLOW + gamesPlayed);
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Please enter a valid username.");
            }
        }
    }
}
