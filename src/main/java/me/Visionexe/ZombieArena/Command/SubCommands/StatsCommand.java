package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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
    public String getPermission() { return "zombiearena.player"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                PlayerWrapper playerWrapper = PlayerWrapper.get(player);
                player.sendMessage(ChatColor.GREEN + "================================");
                player.sendMessage(ChatColor.GREEN + "Stats for " + ChatColor.YELLOW + player.getName());
                player.sendMessage(ChatColor.GREEN + "Level: " + playerWrapper.getLevel());
                player.sendMessage(ChatColor.GREEN + "Experience: " + playerWrapper.getExperience());
                player.sendMessage(ChatColor.GREEN + "Prestige: " + playerWrapper.getPrestige());
                player.sendMessage(ChatColor.GREEN + "Games Played: " + playerWrapper.getGamesPlayed());
                player.sendMessage(ChatColor.GREEN + "Games Won: " + playerWrapper.getGamesWon());
                player.sendMessage(ChatColor.GREEN + "Total Kills: " + playerWrapper.getTotalKills());
                player.sendMessage(ChatColor.GREEN + "================================");

            } else {
                commandSender.sendMessage("Please specify a player!");
            }
        } else if (args.length == 2) {
            Player getPlayer = Bukkit.getPlayer(args[1]);
            if (getPlayer != null) {
                PlayerWrapper playerWrapper = PlayerWrapper.get(getPlayer);
                commandSender.sendMessage(ChatColor.GREEN + "================================");
                commandSender.sendMessage(ChatColor.GREEN + "Stats for " + ChatColor.YELLOW + getPlayer.getName());
                commandSender.sendMessage(ChatColor.GREEN + "Level: " + playerWrapper.getLevel());
                commandSender.sendMessage(ChatColor.GREEN + "Experience: " + playerWrapper.getExperience());
                commandSender.sendMessage(ChatColor.GREEN + "Prestige: " + playerWrapper.getPrestige());
                commandSender.sendMessage(ChatColor.GREEN + "Games Played: " + playerWrapper.getGamesPlayed());
                commandSender.sendMessage(ChatColor.GREEN + "Games Won: " + playerWrapper.getGamesWon());
                commandSender.sendMessage(ChatColor.GREEN + "Total Kills: " + playerWrapper.getTotalKills());
                commandSender.sendMessage(ChatColor.GREEN + "================================");
            } else {
                commandSender.sendMessage(ChatColor.RED + "Please enter a valid username.");
            }
        }
    }
}
