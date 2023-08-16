package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
    public void perform(Player player, String[] args) {
        if (args.length == 3) {
            Player getPlayer = Bukkit.getPlayer(args[1]);
            int exp = Integer.parseInt(args[2]);

            if (getPlayer != null) {
                PlayerWrapper playerWrapper = PlayerWrapper.get(getPlayer);
                if (playerWrapper.setExperience(exp)) {
                    if (getPlayer.equals(player)) {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your EXP has been set to: " + ChatColor.YELLOW + exp);
                    } else {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your EXP has been set to: " + ChatColor.YELLOW + exp);
                        player.sendMessage(
                                ChatColor.YELLOW + player.getName() + "'s" +
                                        ChatColor.GREEN + " exp has been set to " +
                                        ChatColor.YELLOW + exp);
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Please enter a valid username.");
            }
        }
    }
}
