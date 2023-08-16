package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
    public void perform(Player player, String[] args) {
        if (args.length == 3) {
            Player getPlayer = Bukkit.getPlayer(args[1]);
            int prestige = Integer.parseInt(args[2]);

            if (getPlayer != null) {
                PlayerWrapper playerWrapper = PlayerWrapper.get(getPlayer);
                if (playerWrapper.setPrestige(prestige)) {
                    if (getPlayer.equals(player)) {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your prestige has been set to: " + ChatColor.YELLOW + prestige);
                    } else {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your prestige has been set to: " + ChatColor.YELLOW + prestige);
                        player.sendMessage(
                                ChatColor.YELLOW + player.getName() +
                                        ChatColor.GREEN + " has been set to prestige " +
                                        ChatColor.YELLOW + prestige);
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Please enter a valid username.");
            }
        }
    }
}
