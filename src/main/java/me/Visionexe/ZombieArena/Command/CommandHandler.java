package me.Visionexe.ZombieArena.Command;

import me.Visionexe.ZombieArena.Command.SubCommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandHandler implements CommandExecutor {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandHandler() {
        subcommands.add(new StatsCommand());
        subcommands.add(new SetLevelCommand());
        subcommands.add(new SetExperienceCommand());
        subcommands.add(new SetPrestigeCommand());
        subcommands.add(new SetGamesPlayedCommand());
        subcommands.add(new SetGamesWonCommand());
        subcommands.add(new SetTotalKillsCommand());
        subcommands.add(new CreateArenaCommand());
        subcommands.add(new ArenaSetPlayerSpawnCommand());
        subcommands.add(new ArenaAddMobSpawnPointCommand());
        subcommands.add(new JoinArenaCommand());
        subcommands.add(new StartGameCommand());
        subcommands.add(new SetWaveCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        if (player.hasPermission(getSubcommands().get(i).getPermission())) {
                            getSubcommands().get(i).perform(player, args);
                        }
                    }
                }
            } else if(args.length == 0) {
                player.sendMessage(ChatColor.GREEN + "--------------------------------");
                for (int i = 0; i < getSubcommands().size(); i++) {
                    player.sendMessage(ChatColor.YELLOW + getSubcommands().get(i).getSyntax() + " - " +
                            ChatColor.GREEN + getSubcommands().get(i).getDescription());
                }
                player.sendMessage(ChatColor.GREEN + "--------------------------------");
            }
        }
        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

}
