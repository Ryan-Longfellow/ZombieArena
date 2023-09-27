package me.Visionexe.ZombieArena.Command;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    // Name of the sub command, Ex: /zombiearena <command>
    public abstract String getName();

    // Description of the sub command, Ex: /zombiearena setlevel will set the level of a player
    public abstract String getDescription();

    // How to use the subcommand, Ex: /zombiearena setlevel <player> <level>
    public abstract String getSyntax();

    // Permission the player must have to use the command
    public abstract String getPermission();

    // Code to execute for said sub command
    public abstract void perform(CommandSender commandSender, String args[]);

}
