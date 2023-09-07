package me.Visionexe.ZombieArena.Game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PlayerStats implements Comparable<PlayerStats> {
    private String player;
    private String arenaName;
    private boolean alive;
    private long deathTime;

    public PlayerStats(Player player) {
        this.player = player.getName();
        this.alive = false;
        resetStats();
    }

    public Player getPlayer() { return Bukkit.getPlayer(player); }

    public String getArenaName() { return this.arenaName; }

    public int getTimeSinceDeath() {
        /*
        The +1 is to ensure this never returns 0. This prevents the plugin from sending the player a message on death while
        simultaneously sending them a message updating the time until the respawn.
         */
        return (int) (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - TimeUnit.MILLISECONDS.toSeconds(deathTime) + 1);
    }

    public boolean isAlive() { return alive; }

    public void registerDeath() { deathTime = System.currentTimeMillis(); }

    public void resetStats() {
        // This may eventually include more information but this is it for now
        deathTime = System.currentTimeMillis();
    }

    public void setArenaName(String arenaName) { this.arenaName = arenaName; }

    public void setAlive(boolean alive) { this.alive = alive; }

    @Override
    public int compareTo(PlayerStats stats) {
        if (this.getPlayer() == null && stats.getPlayer() == null) { return 0; }
        if (this.getPlayer() == null) { return 1; }
        if (stats.getPlayer() == null) { return -1; }
        if (stats.getPlayer().getHealth() > this.getPlayer().getHealth()) { return 1; }
        else if (stats.getPlayer().getHealth() == this.getPlayer().getHealth()) { return 0; }
        return -1;
    }
}
