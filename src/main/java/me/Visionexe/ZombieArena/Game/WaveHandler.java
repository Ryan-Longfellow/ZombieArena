package me.Visionexe.ZombieArena.Game;

import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveHandler {
    // As the name implies, this will handle the wave system within the game
    // There are to be 50 waves, every 10 waves there will be a miniboss
    // At wave 50 there will be a Warden/Wither boss (unsure which one should be last boss)
    private ZombieArena plugin;
    private GameHandler gameHandler;

    private double spawnChance;
    private Random random;

    private int timeUntilNextWave;
    private int wave;
    private int mobsToSpawn;
    private List<EntityType> entities;

    private int seconds;
    private int secondsWithFewEntities;

    public WaveHandler(GameHandler instance) {
        plugin = ZombieArena.getInstance();
        gameHandler = instance;
        random = new Random();
        wave = 0;
        entities = new ArrayList<>();
    }

    private void attemptSpawnEntity() {
        if (mobsToSpawn <= 0) { return; }

        // Get the world of the arena
        // Get a random mob spawn location
        // Create a zombie entity, set health and name
        // Spawn zombie

        mobsToSpawn--;
    }

    public void start() {

    }

    public int getWave() { return wave; }
}
