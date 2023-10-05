package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.Utils.Check;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class AddExperienceCommand extends SubCommand {
    @Override
    public String getName() {
        return "addexp";
    }

    @Override
    public String getDescription() {
        return "Add experience to a given player.";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena addexp <player> <amount>";
    }

    @Override
    public String getPermission() {
        return "zombiearena.admin";
    }

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
            // Since both have been confirmed, assign accordingly and obtain PlayerWrapper
            Player getPlayer = Bukkit.getPlayer(args[1]);
            int exp = Integer.parseInt(args[2]);
            PlayerWrapper playerWrapper = PlayerWrapper.get(Objects.requireNonNull(getPlayer));

            // Ensure experience is valid (greater than 0); if so, add exp
            if (playerWrapper.addExperience(exp)) {
                // If the player sending the command is the one being edited, only send a message to the command sender
                if (getPlayer.equals(commandSender)) {
                    getPlayer.sendMessage(ChatColor.GREEN + "You received " + ChatColor.YELLOW + exp + ChatColor.GREEN + " EXP");
                } else {
                    // If the player having exp added is not the one sending the command, notify both of the change
                    getPlayer.sendMessage(ChatColor.GREEN + "You received " + ChatColor.YELLOW + exp + ChatColor.GREEN + " EXP");
                    commandSender.sendMessage(
                            ChatColor.YELLOW + "" + exp +
                                    ChatColor.GREEN + " exp has been added to " +
                                    ChatColor.YELLOW + getPlayer.getName());
                }
            }
        } else {
            commandSender.sendMessage("Please follow the following syntax: " + getSyntax());
        }
    }
}
