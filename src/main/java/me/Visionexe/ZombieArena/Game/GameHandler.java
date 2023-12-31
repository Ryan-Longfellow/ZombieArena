package me.Visionexe.ZombieArena.Game;

import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.Event.PlayerRespawnCause;
import me.Visionexe.ZombieArena.Event.PlayerRespawnInGameEvent;
import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class GameHandler {
    private ZombieArena plugin;
    private WaveHandler waveHandler;
    private ArenaHandler arenaHandler;
    private boolean isRunning;
    private boolean isWaiting;
    private int maxPlayers;
    private GameDifficulty difficulty;
    private List<String> players;
    private Map<String, PlayerStats> playerStats;
    private Location lobbySpawn;
    private String arenaName;

    public GameHandler(GameDifficulty gameDifficulty) {
        plugin = ZombieArena.getInstance();
        lobbySpawn = Objects.requireNonNull(Bukkit.getWorld(Objects.requireNonNull(plugin.getConfigFile().getString("LobbyWorld")))).getSpawnLocation();
        waveHandler = new WaveHandler(this);
        arenaHandler = new ArenaHandler();
        ArenaHandler.loadArenas();
        difficulty = gameDifficulty;
        isRunning = false;
        isWaiting = true;
        maxPlayers = plugin.getConfigFile().getInt("max-players");
        players = new ArrayList<>();
        playerStats = new HashMap<>();
    }

    public void addPlayer(Player player, String arenaName) {
        if (players.contains(player.getName())) {
            Log.debug("Player already found in game.");
            return;
        }
        if (players.size() >= maxPlayers) {
            player.sendMessage("Game is full!");
            return;
        }
        players.add(player.getName());
        Log.debug("Added player to game list");
        PlayerStats stats = new PlayerStats(player);
        this.arenaName = arenaName;
        stats.setArenaName(arenaName);
        playerStats.put(player.getName(), stats);
        Log.debug("Added player stats to game list");

        player.setGameMode(GameMode.ADVENTURE);
        healPlayer(player);
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
                PlayerRespawnInGameEvent event = new PlayerRespawnInGameEvent(stats.getPlayer(), PlayerRespawnCause.CUSTOM);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    addToGame(stats);
                }
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
        healPlayer(player);

        PlayerWrapper.get(player).addGamesPlayed(1);
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
        List<Player> playerInstances = new ArrayList<>();
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
    public GameDifficulty getGameDifficulty() { return this.difficulty; }

    public boolean isRunning() { return isRunning; }
    public boolean isWaiting() { return isWaiting; }
    public int getMaxPlayers() { return maxPlayers; }

    public void removePlayer(String playerName) {
        if (players.contains(playerName)) {
            players.remove(playerName);
            playerStats.remove(playerName);
            Player player = Bukkit.getPlayer(playerName);

            player.teleport(lobbySpawn);
            healPlayer(player);
        };
    }

    public void removePlayer(Player player) {
        if (players.contains(player.getName())) {
            players.remove(player.getName());
            playerStats.remove(player.getName());

            player.teleport(lobbySpawn);
            healPlayer(player);
        }
    }

    public void respawnPlayer(Player player) {
        if (!(getPlayerStats(player).isAlive())) {
            getPlayerStats(player).setAlive(true);
            // Teleport to arena spawn
            player.teleport(arenaHandler.getPlayerSpawn(getPlayerStats(player).getArenaName()));
            // Confirm player is in adventure mode
            player.setGameMode(GameMode.ADVENTURE);
            // Set player to max health, food and saturation
            healPlayer(player);
        }
//        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("add text here"));
    }

    /*
    Sets player to max health, food and saturation and removes all petition effects
     */
    public void healPlayer(Player player) {
        double playerMaxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        if (playerMaxHealth < 20.0) playerMaxHealth = 20.0;
        player.sendHealthUpdate(playerMaxHealth, 20, 20);
        player.setHealth(playerMaxHealth);
        for (PotionEffect potion : player.getActivePotionEffects()) {
            player.removePotionEffect(potion.getType());
        }
    }

    public void healAllPlayers() {
        for (String player : players) {
            healPlayer(Objects.requireNonNull(Bukkit.getPlayer(player)));
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

        waveHandler.removeEntities();
        waveHandler.stop();
        String arenaName = this.arenaName;

        for (PlayerStats stats : playerStats.values()) {
            if (!(stats.isAlive())) respawnPlayer(stats.getPlayer());
            stats.resetStats();
            stats.setAlive(false);

            Player player = stats.getPlayer();
            if (player != null) {
                player.teleport(lobbySpawn);
                player.setGameMode(GameMode.ADVENTURE);
                healPlayer(player);
                removePlayer(player);
            }
        }
        ZombieArena.getInstance().getGames().remove(arenaName + "_" + getGameDifficulty().toString().toLowerCase());
    }
}
