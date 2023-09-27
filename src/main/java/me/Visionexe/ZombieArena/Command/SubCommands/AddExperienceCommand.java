package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
    public void perform(Player player, String[] args) {
        if (args.length == 3) {
            Player getPlayer = Bukkit.getPlayer(args[1]);
            int exp = Integer.parseInt(args[2]);

            if (getPlayer != null) {
                PlayerWrapper playerWrapper = PlayerWrapper.get(getPlayer);
                if (playerWrapper.addExperience(exp)) {
                    if (getPlayer.equals(player)) {
                        getPlayer.sendMessage(ChatColor.GREEN + "You received " + ChatColor.YELLOW + exp + ChatColor.GREEN + " EXP");
                    } else {
                        getPlayer.sendMessage(ChatColor.GREEN + "You received " + ChatColor.YELLOW + exp + ChatColor.GREEN + " EXP");
                        player.sendMessage(
                                ChatColor.YELLOW + "" + exp +
                                        ChatColor.GREEN + " exp has been added to " +
                                        ChatColor.YELLOW + player.getName());
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Please enter a valid username.");
            }
        }
    }
}
