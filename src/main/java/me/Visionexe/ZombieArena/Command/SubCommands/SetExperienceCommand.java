package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.Utils.Check;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetExperienceCommand extends SubCommand {
    @Override
    public String getName() {
        return "setexp";
    }

    @Override
    public String getDescription() {
        return "Set experience for a given player.";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena setexp <player> <experience>";
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
                commandSender.sendMessage("Please enter a valid experience integer");
                return;
            }
            Player getPlayer = Bukkit.getPlayer(args[1]);
            int exp = Integer.parseInt(args[2]);
            PlayerWrapper playerWrapper = PlayerWrapper.get(Objects.requireNonNull(getPlayer));

            if (playerWrapper.setExperience(exp)) {
                if (getPlayer.equals(commandSender)) {
                    getPlayer.sendMessage(ChatColor.GREEN + "Your EXP has been set to: " + ChatColor.YELLOW + exp);
                } else {
                    getPlayer.sendMessage(ChatColor.GREEN + "Your EXP has been set to: " + ChatColor.YELLOW + exp);
                    commandSender.sendMessage(
                            ChatColor.YELLOW + getPlayer.getName() + "'s" +
                                    ChatColor.GREEN + " exp has been set to " +
                                    ChatColor.YELLOW + exp);
                }
            }
        } else {
            commandSender.sendMessage("Please follow the following syntax: " + getSyntax());
        }
    }
}
