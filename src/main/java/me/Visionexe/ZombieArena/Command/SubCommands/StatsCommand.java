package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.GUI.Game.Stats;
import me.Visionexe.ZombieArena.GUI.Row;
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
        return "/stats [player]";
    }

    @Override
    public String getPermission() { return "zombiearena.player"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 1) {
                    Stats statsGUI = new Stats(ChatColor.translateAlternateColorCodes('&', "&e&l" + player.getName() + "&f's Stats"), Row.THREE, player);
                    statsGUI.open(player);
            } else if (args.length == 2) {
                Player getPlayer = Bukkit.getPlayer(args[1]);
                if (getPlayer != null) {
                    Stats statsGUI = new Stats(ChatColor.translateAlternateColorCodes('&', "&e&l" + getPlayer.getName() + "&f's Stats"), Row.THREE, getPlayer);
                    statsGUI.open((Player) commandSender);
                } else {
                    commandSender.sendMessage(ChatColor.RED + "Please enter a valid username.");
                }
            } else {
                player.sendMessage("Please follow the following syntax: " + getSyntax());
            }
        } else {
            commandSender.sendMessage("Only players are allowed to use this command!");
        }
    }
}
