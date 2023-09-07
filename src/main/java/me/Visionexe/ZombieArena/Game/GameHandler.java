package me.Visionexe.ZombieArena.Game;

import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameHandler {
    // As the name implies, this handles all the overall information for the game itself
    private ZombieArena plugin;
    private WaveHandler waveHandler;
    private ArenaHandler arenaHandler;
    private boolean isRunning;
    private boolean isWaiting;
    private List<String> players;
    private Map<String, PlayerStats> playerStats;

    public GameHandler() {
        plugin = ZombieArena.getInstance();
        waveHandler = new WaveHandler(this);
        arenaHandler = new ArenaHandler();
        arenaHandler.loadArenas();
        isRunning = false;
        isWaiting = true;
        players = new ArrayList<>();
        playerStats = new HashMap<String, PlayerStats>();
    }

    public void addPlayer(Player player) {
        if (players.contains(player.getName())) return;
        players.add(player.getName());
        PlayerStats stats = new PlayerStats(player);
        playerStats.put(player.getName(), stats);

        player.setGameMode(GameMode.ADVENTURE);
        int wave = waveHandler.getWave();
        if (isRunning) {
            if (wave == 1) {
                addToGame(stats);
            } else {
                // Create a function to put player into spectator mode and teleport to arena spawn.
                // Have player wait 5 seconds then spawn in adventure back at arena spawn again.
            }
        } else if (isWaiting) {
            start();
        }
    }

    private void addToGame(PlayerStats stats) {
        stats.resetStats();
        stats.setAlive(true);

        Player player = stats.getPlayer();
        if (player == null) return;
        // Teleport player to arena spawn

        // Set player to max health, food and saturation
        player.sendHealthUpdate(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), 20, 20);
        // Remove any active potion effects
        for (PotionEffect potion : player.getActivePotionEffects()) {
            player.removePotionEffect(potion.getType());
        }
    }

    public int getAliveCount() {
        int alive = 0;
        for (PlayerStats stats : playerStats.values()) {
            if (stats.isAlive()) { alive++; }
        }
        return alive;
    }

    public synchronized List<String> getPlayerNames() { return players; }

    public synchronized List<Player> getPlayers() {
        List<Player> playerInstances = new ArrayList<Player>();
        for (String playerName : players) {
            Player player = Bukkit.getPlayer(playerName);
            if (player != null) { playerInstances.add(player); }
        }
        return playerInstances;
    }

    public synchronized Map<String, PlayerStats> getPlayerStats() { return playerStats; }

    public synchronized PlayerStats getPlayerStats(String player) { return playerStats.get(player); }

    public synchronized PlayerStats getPlayerStats(Player player) { return playerStats.get(player.getName()); }

    public WaveHandler getWaveHandler() { return waveHandler; }

    public boolean isRunning() { return isRunning; }
    public boolean isWaiting() { return isWaiting; }

    public void removePLayer(Player player) {
        if (players.contains(player.getName())) {
            players.remove(player.getName());
            playerStats.remove(player.getName());

            player.teleport(player.getWorld().getSpawnLocation());
        }
    }

    public void respawnPlayer(Player player) {
        if (!getPlayerStats(player).isAlive()) {
            getPlayerStats(player).setAlive(true);
            // Teleport to arena spawn

            // Set player to max health, food and saturation
            player.sendHealthUpdate(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), 20, 20);
            // Remove any active potion effects
            for (PotionEffect potion : player.getActivePotionEffects()) {
                player.removePotionEffect(potion.getType());
            }
        }
    }

    public void start() {
        if (isRunning) { return; }
        if (players.isEmpty()) {
            isWaiting = true;
            return;
        }

        for (PlayerStats stats : playerStats.values()) {
            addToGame(stats);
        }
        isRunning = true;
        waveHandler.start();
    }

    public void stop() {
        if (!isRunning) { return; }
        isRunning = false;
        isWaiting = true;

        for (PlayerStats stats : playerStats.values()) {
            stats.resetStats();
            stats.setAlive(false);

            Player player = stats.getPlayer();
            if (player != null) {
                for (PotionEffect potion : player.getActivePotionEffects()) {
                    player.removePotionEffect(potion.getType());
                }
            }
        }
    }

}
