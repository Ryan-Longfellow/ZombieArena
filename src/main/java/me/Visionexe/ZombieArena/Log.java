package me.Visionexe.ZombieArena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Log {


    /**
     * Sends a developer a message.
     *
     * @param message message to send
     */
    public static void debug(String message) {
        debug(message, Level.INFO);
    }

    /**
     * Sends a message of the specified level.
     *
     * @param message message to send
     * @param level   level of the message
     */
    public static void debug(String message, Level level) {
        if (!isDebug()) {
            return;
        }
        if (level == Level.DEVELOPER) {
            Player player = Bukkit.getPlayer(UUID.fromString("43d26e4e-fcd8-48dd-8f01-2c1f8f0fb9e3"));
            if (player != null) {
                player.sendMessage(format(message, level));
            }
        } else {
            Bukkit.broadcastMessage(format(message, level));
        }
    }


    /**
     * Formats the debug message.
     *
     * @param message message to format
     * @param level   log level of the message
     *
     * @return formatted string
     */
    private static String format(String message, Level level) {
        return ChatColor.GOLD + "" + ChatColor.BOLD + "[DEBUG] " + ChatColor.RESET + "" + ChatColor.GOLD + "[" + level.name().toUpperCase() + "]: " + ChatColor.RESET + "" + ChatColor.GRAY + message;
    }

    /**
     * Checks whether debug mode is enabled or not.
     *
     * @return True if debug mode is enabled, false otherwise.
     *
     * @since 1.0.0
     */
    public static boolean isDebug() {
        return ZombieArena.getInstance().getFileManager().get("config").get().getConfiguration().getBoolean("debug");
    }

    /**
     * Log level
     */
    public enum Level {
        INFO,
        WARNING,
        ERROR,
        DEVELOPER;
    }
}
