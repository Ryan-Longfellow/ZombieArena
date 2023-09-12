package me.Visionexe.ZombieArena.Command.SubCommands;

import me.Visionexe.ZombieArena.Command.SubCommand;
import me.Visionexe.ZombieArena.GUI.Blacksmith.Blacksmith;
import me.Visionexe.ZombieArena.GUI.Row;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BlacksmithCommand extends SubCommand {
    @Override
    public String getName() {
        return "blacksmith";
    }

    @Override
    public String getDescription() {
        return "Opens the blacksmith menu";
    }

    @Override
    public String getSyntax() {
        return "/zombiearena blacksmith";
    }

    @Override
    public String getPermission() {
        return "zombiearena.player";
    }

    @Override
    public void perform(Player player, String[] args) {
        Blacksmith gui = new Blacksmith(ChatColor.translateAlternateColorCodes('&', "&aBlacksmith"), Row.THREE);
        gui.open(player);
    }
}