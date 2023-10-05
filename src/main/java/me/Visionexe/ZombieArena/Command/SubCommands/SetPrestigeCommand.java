package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.Utils.Check;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetPrestigeCommand extends SubCommand {
    @Override
    public String getName() {
        return "setprestige";
    }

    @Override
    public String getDescription() {
        return "Set prestige for the given player.";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena setprestige <player> <prestige>";
    }

    @Override
    public String getPermission() { return "zombiearena.admin"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (args.length == 3) {
            // Ensure player is valid
            if (Bukkit.getPlayer(args[1]) == null) {
                commandSender.sendMessage("Please enter a valid player name");
                return;
            }
            // Ensure experience value passed is a valid integer
            if (!(Check.isInteger(args[2]))) {
                commandSender.sendMessage("Please enter a valid prestige integer");
                return;
            }
            Player getPlayer = Bukkit.getPlayer(args[1]);
            int prestige = Integer.parseInt(args[2]);
            PlayerWrapper playerWrapper = PlayerWrapper.get(Objects.requireNonNull(getPlayer));
            if (playerWrapper.setPrestige(prestige)) {
                if (getPlayer.equals(commandSender)) {
                    getPlayer.sendMessage(ChatColor.GREEN + "Your prestige has been set to: " + ChatColor.YELLOW + prestige);
                } else {
                    getPlayer.sendMessage(ChatColor.GREEN + "Your prestige has been set to: " + ChatColor.YELLOW + prestige);
                    commandSender.sendMessage(
                            ChatColor.YELLOW + getPlayer.getName() +
                                    ChatColor.GREEN + " has been set to prestige " +
                                    ChatColor.YELLOW + prestige);
                }
            }
        } else {
            commandSender.sendMessage("Please follow the following syntax: " + getSyntax());
        }
    }
}
