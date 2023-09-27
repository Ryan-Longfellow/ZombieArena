package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGamesWonCommand extends SubCommand {
    @Override
    public String getName() {
        return "setgameswon";
    }

    @Override
    public String getDescription() {
        return "Set games won for the given player.";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena setgameswon <player> <gameswon>";
    }

    @Override
    public String getPermission() { return "zombiearena.admin"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (args.length == 3) {
            Player getPlayer = Bukkit.getPlayer(args[1]);
            int gamesWon = Integer.parseInt(args[2]);

            if (getPlayer != null) {
                PlayerWrapper playerWrapper = PlayerWrapper.get(getPlayer);
                if (playerWrapper.setGamesWon(gamesWon)) {
                    if (getPlayer.equals(commandSender)) {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your games won has been set to: " + ChatColor.YELLOW + gamesWon);
                    } else {
                        getPlayer.sendMessage(ChatColor.GREEN + "Your games won has been set to: " + ChatColor.YELLOW + gamesWon);
                        commandSender.sendMessage(
                                ChatColor.YELLOW + getPlayer.getName() + "'s" +
                                        ChatColor.GREEN + " games won has been set to " +
                                        ChatColor.YELLOW + gamesWon);
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Please enter a valid username.");
            }
        }
    }
}
