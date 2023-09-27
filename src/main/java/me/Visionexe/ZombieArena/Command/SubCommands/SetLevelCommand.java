package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLevelCommand extends SubCommand {
    @Override
    public String getName() {
        return "setlevel";
    }

    @Override
    public String getDescription() {
        return "Set level of a given player.";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena setlevel <player> <level>";
    }

    @Override
    public String getPermission() { return "zombiearena.admin"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (args.length == 3) {
            Player getPlayer = Bukkit.getPlayer(args[1]);
            int level = Integer.parseInt(args[2]);

            if (getPlayer != null) {
                PlayerWrapper playerWrapper = PlayerWrapper.get(getPlayer);
                if (playerWrapper.setLevel(level)) {
                    if (getPlayer.equals(commandSender)) {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your Level has been set to: " + ChatColor.YELLOW + level);
                    } else {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your Level has been set to: " + ChatColor.YELLOW + level);
                        commandSender.sendMessage(
                                ChatColor.YELLOW + getPlayer.getName() +
                                        ChatColor.GREEN + " has been set to level " +
                                        ChatColor.YELLOW + level);
                    }
                } else {
                    commandSender.sendMessage(ChatColor.RED + "Level must be between 1 and " + playerWrapper.getMaxLevel());
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Please enter a valid username.");
            }
        }
    }
}
