package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTotalKillsCommand extends SubCommand {
    @Override
    public String getName() {
        return "settotalkills";
    }

    @Override
    public String getDescription() {
        return "Set the total kills for a given player.";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena settotalkills <player> <totalkills>";
    }

    @Override
    public String getPermission() { return "zombiearena.admin"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (args.length == 3) {
            Player getPlayer = Bukkit.getPlayer(args[1]);
            int totalKills = Integer.parseInt(args[2]);

            if (getPlayer != null) {
                PlayerWrapper playerWrapper = PlayerWrapper.get(getPlayer);
                if (playerWrapper.setTotalKills(totalKills)) {
                    if (getPlayer.equals(commandSender)) {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your total kills has been set to: " + ChatColor.YELLOW + totalKills);
                    } else {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your total kills has been set to: " + ChatColor.YELLOW + totalKills);
                        commandSender.sendMessage(
                                ChatColor.YELLOW + getPlayer.getName() + "'s" +
                                        ChatColor.GREEN + " total kills has been set to " +
                                        ChatColor.YELLOW + totalKills);
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Please enter a valid username.");
            }
        }
    }
}
