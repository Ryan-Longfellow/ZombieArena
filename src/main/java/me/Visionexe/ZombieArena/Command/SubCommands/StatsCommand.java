package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StatsCommand extends SubCommand {
    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "Return stats of a given player. If no player is given, return stats for self";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena stats [player]";
    }

    @Override
    public String getPermission() { return "zombiearena.stats"; }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 1) {
            PlayerWrapper playerWrapper = PlayerWrapper.get(player);
            player.sendMessage(ChatColor.GREEN + "================================");
            player.sendMessage(ChatColor.GREEN + "Stats for " + ChatColor.YELLOW + player.getName());
            player.sendMessage(ChatColor.GREEN + "Level: " + String.valueOf(playerWrapper.getLevel()));
            player.sendMessage(ChatColor.GREEN + "Experience: " + String.valueOf(playerWrapper.getExperience()));
            player.sendMessage(ChatColor.GREEN + "Prestige: " + String.valueOf(playerWrapper.getPrestige()));
            player.sendMessage(ChatColor.GREEN + "Games Played: " + String.valueOf(playerWrapper.getGamesPlayed()));
            player.sendMessage(ChatColor.GREEN + "Games Won: " + String.valueOf(playerWrapper.getGamesWon()));
            player.sendMessage(ChatColor.GREEN + "Total Kills: " + String.valueOf(playerWrapper.getTotalKills()));
            player.sendMessage(ChatColor.GREEN + "================================");
        } else if (args.length == 2) {
            Player getPlayer = Bukkit.getPlayer(args[1]);
            if (getPlayer != null) {
                PlayerWrapper playerWrapper = PlayerWrapper.get(getPlayer);
                player.sendMessage(ChatColor.GREEN + "================================");
                player.sendMessage(ChatColor.GREEN + "Stats for " + ChatColor.YELLOW + getPlayer.getName());
                player.sendMessage(ChatColor.GREEN + "Level: " + String.valueOf(playerWrapper.getLevel()));
                player.sendMessage(ChatColor.GREEN + "Experience: " + String.valueOf(playerWrapper.getExperience()));
                player.sendMessage(ChatColor.GREEN + "Prestige: " + String.valueOf(playerWrapper.getPrestige()));
                player.sendMessage(ChatColor.GREEN + "Games Played: " + String.valueOf(playerWrapper.getGamesPlayed()));
                player.sendMessage(ChatColor.GREEN + "Games Won: " + String.valueOf(playerWrapper.getGamesWon()));
                player.sendMessage(ChatColor.GREEN + "Total Kills: " + String.valueOf(playerWrapper.getTotalKills()));
                player.sendMessage(ChatColor.GREEN + "================================");
            } else {
                player.sendMessage(ChatColor.RED + "Please enter a valid username.");
            }
        }
    }
}
