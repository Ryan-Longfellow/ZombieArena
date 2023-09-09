package me.Visionexe.ZombieArena.Game;

import me.Visionexe.ZombieArena.Event.PlayerRespawnCause;
import me.Visionexe.ZombieArena.Event.PlayerRespawnInGameEvent;
import me.Visionexe.ZombieArena.Log;
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

    public void addPlayer(Player player, String arenaName) {
        if (players.contains(player.getName())) {
            Log.debug("Player already found in game.");
            return;
        };
        players.add(player.getName());
        Log.debug("Added player to game list");
        PlayerStats stats = new PlayerStats(player);
        stats.setArenaName(arenaName);
        playerStats.put(player.getName(), stats);
        Log.debug("Added player stats to game list");

        player.setGameMode(GameMode.ADVENTURE);
        Log.debug("Player set to adventure mode");
        int wave = waveHandler.getWave();
        Log.debug("Current wave: " + wave);
        if (isRunning) {
            if (wave == 1) {
                PlayerRespawnInGameEvent event = new PlayerRespawnInGameEvent(stats.getPlayer(), PlayerRespawnCause.GAME_START);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    addToGame(stats);
                }
            } else {
                // Create a function to put player into spectator mode and teleport to arena spawn.
                // Have player wait 5 seconds then spawn in adventure back at arena spawn again.
            }
        } else {
            Log.debug("Game is waiting. Starting game...");
            start();
        }
    }

    private void addToGame(PlayerStats stats) {
        stats.resetStats();
        stats.setAlive(true);

        Player player = stats.getPlayer();
        if (player == null) return;
        // Teleport player to arena spawn
        player.teleport(arenaHandler.getPlayerSpawn(stats.getArenaName()));

        // Set player to max health, food and saturation
        player.sendHealthUpdate(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), 20, 20);
        // Remove any active potion effects
        for (PotionEffect potion : player.getActivePotionEffects()) {
            player.removePotionEffect(potion.getType());
        }

        // TODO: Call scoreboard class to add lines for game
        // Waves, mobs remaining, time to next wave if available
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

    public WaveHandler getWaveHandler() { return this.waveHandler; }
    public ArenaHandler getArenaHandler() { return this.arenaHandler; }

    public boolean isRunning() { return isRunning; }
    public boolean isWaiting() { return isWaiting; }

    public void removePlayer(String playerName) {
        players.remove(playerName);
        playerStats.remove(playerName);
    }

    public void removePlayer(Player player) {
        if (players.contains(player.getName())) {
            players.remove(player.getName());
            playerStats.remove(player.getName());

            player.teleport(player.getWorld().getSpawnLocation());

            // TODO: Remove scoreboard information from game
        }
    }

    public void respawnPlayer(Player player) {
        if (!getPlayerStats(player).isAlive()) {
            getPlayerStats(player).setAlive(true);
            // Teleport to arena spawn
            player.teleport(arenaHandler.getPlayerSpawn(getPlayerStats(player).getArenaName()));

            // Set player to max health, food and saturation
            player.sendHealthUpdate(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), 20, 20);
            // Remove any active potion effects
            for (PotionEffect potion : player.getActivePotionEffects()) {
                player.removePotionEffect(potion.getType());
            }
        }
    }

    public void start() {
        Log.debug("Starting game...");
        if (isRunning) {
            Log.debug("Game is already running");
            return;
        }
        if (players.isEmpty()) {
            isWaiting = true;
            Log.debug("No players are in the game");
            return;
        }

        for (PlayerStats stats : playerStats.values()) {
            Log.debug("Attempting to add " + stats.getPlayer().getName() + " to the game");
            PlayerRespawnInGameEvent event = new PlayerRespawnInGameEvent(stats.getPlayer(), PlayerRespawnCause.GAME_START);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()) {
                addToGame(stats);
            }
        }
        isRunning = true;
        Log.debug("Starting waves...");
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
