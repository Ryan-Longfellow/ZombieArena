package me.Visionexe.ZombieArena.Game;

import me.Visionexe.ZombieArena.Event.GameStopCause;
import me.Visionexe.ZombieArena.Event.GameStopEvent;
import me.Visionexe.ZombieArena.Event.WaveChangeEvent;
import me.Visionexe.ZombieArena.Event.WaveStartEvent;
import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class WaveHandler implements Runnable, Listener {
    // As the name implies, this will handle the wave system within the game
    // There are to be 50 waves, every 10 waves there will be a miniboss
    // At wave 50 there will be a Warden/Wither boss (unsure which one should be last boss)
    private ZombieArena plugin;
    private GameHandler gameHandler;

    private double spawnChance;
    private Random random;

    private int timeUntilNextWave;
    private int wave;
    private int maxWave = 50;
    private int mobsToSpawn;
    private Map<Zombie, Integer> entities;

    int taskID = -1;
    private int seconds;
    private int secondsWithFewEntities;

    public WaveHandler(GameHandler instance) {
        plugin = ZombieArena.getInstance();
        gameHandler = instance;
        random = new Random();
        wave = 0;
        entities = new HashMap<>();
    }

    private void attemptSpawnEntity() {
        if (mobsToSpawn <= 0) { return; }

        ArenaHandler arenaHandler = gameHandler.getArenaHandler();
        // A very bad process of getting the mobSpawns of an arena by taking the first player listed in the arena and getting the arena name from them
        List<Location> mobSpawns = arenaHandler.getMobSpawns(gameHandler.getPlayerStats(gameHandler.getPlayers().get(0)).getArenaName());
        // Also probably a bad way to get the world in order to spawn the entity; this just gets the world of the location
        // It should always be the same as the arena anyway
        World world = mobSpawns.get(0).getWorld();
        // Create a Zombie Entity to spawn at a random mobSpawn location
        Zombie zombie = (Zombie) world.spawnEntity(mobSpawns.get(random.nextInt(mobSpawns.size())), EntityType.ZOMBIE);
        zombie.setAdult(); // Make sure zombie is an adult
        zombie.setCustomName("Test Zombie");
        mobsToSpawn--;
        entities.put(zombie, zombie.getEntityId());
    }

    private boolean checkNextWave() {
        return mobsToSpawn <= 0 && entities.isEmpty() && timeUntilNextWave <= 0;
    }

    public int getGameLength() { return seconds; }

    public int getRemainingZombies() {
        return entities.size() + mobsToSpawn;
    }

    public int getWave() { return wave; }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        /*
        Resets timer for few entities if a player damages the entity or if the entity does damage
        Prevents entities from disappearing in the middle of fighting and a new wave randomly starting
         */
        if(entities.containsValue(event.getEntity().getEntityId())) {
            secondsWithFewEntities = 0;
        }
    }

    public int calcQuantity(int wave) {
        return wave * 5; // Currently just returning the wave * 5 to spawn that many entities; will create something more complex later maybe
    }

    private void prepareNextWave() {
        timeUntilNextWave = 5;
        secondsWithFewEntities = 0;
        Log.debug("Time Until Next Wave Set To: " + timeUntilNextWave + ". Seconds With Few Entities reset");
        mobsToSpawn = calcQuantity(wave);
        Log.debug("Mobs To Spawn Set To: " + mobsToSpawn);
    }

    @Override
    public void run() {
        if(gameHandler.isRunning()) {
            Log.debug("Game is running...");
            if(timeUntilNextWave > 0) {
                timeUntilNextWave--;
                Log.debug("Time until next wave: " + timeUntilNextWave);
                if(timeUntilNextWave <= 0) {
                    Log.debug("Time for next wave ended. Starting wave...");
                    startWave();
                }
            } else {
                /*
                Runs 5 times every second to spawn mob and check if next wave is available
                Will start the next wave is all criteria is met

                This value will need to be changed based off of mobs spawning/spawned
                This causes issues with waves starting constantly after the criteria is met once
                Ex: If i = 10 but mobsToSpawn = 5 / mobsSpawned = 5
                Once all 5 mobs are killed it will start 4 or 5 additional waves due the loop running 5 extra times

                Unsure what exactly can be done with this value
                 */
                for (int i = 0; i < 5; i++) {
                    Log.debug("Attempting to spawn entities...");
                    attemptSpawnEntity();
                    Log.debug("Updating entity list...");
                    Log.debug("Current wave: " + wave);
                    updateEntityList();
                }
                Log.debug("Checking if next wave...");
                Log.debug("Mobs To Spawn: " + mobsToSpawn + ". Entities Empty: " + entities.isEmpty() + ". " + "Time to next wave: " + timeUntilNextWave);

                if(checkNextWave()) {
                    Log.debug("Progressing to next wave...");
                    setWave(wave + 1);
                }

                /*
                Will automatically progress to the next wave is no damage is done within 60 seconds with 5 or less mobs alive
                 */
                if(true) { // There is supposed to be a check here to see if an option is enabled for automatically progressing to next way
                    if(entities.size() <= 5) {
                        secondsWithFewEntities++;
                    }
                    if(secondsWithFewEntities > 60) {
                        setWave(wave + 1);
                    }
                }
                /*
                Can be used in the future to perform an action every 5sec
                 */
                if(seconds % 5 == 0) {
                    // Can add the same update here as from GameHandler to generate a single heart every 5 seconds
                    // Will have to loop through all players in the game from GameHandler.getPlayers()
                }

                /*
                TODO: Add an ActionBar with "Wave: # Alive: # Dead: # Enemies: #"
                Also add a Scoreboard with
                        TITLE
                Info:
                    Name:
                    Level:
                    Exp / Exp to Next Level:
                Stats:
                    Money:
                    Mob kills:
                        ZombieArena IP
                 */
            }

            // To prevent players who do not set off any listeners from staying added into the game
            for(String pName : gameHandler.getPlayerNames()) {
                Player p = Bukkit.getPlayer(pName);
                if(p == null || !p.isOnline()) {
                    gameHandler.removePlayer(pName);
                }
            }

            // End the game if no one is alive
            if(gameHandler.getAliveCount() <= 0) {
                gameHandler.stop();
                GameStopEvent event = new GameStopEvent(GameStopCause.ALL_DEAD);
                Bukkit.getServer().getPluginManager().callEvent(event);
            }

            // Respawn player after 10 seconds if dead
            if(true) { // Will need to configure player respawn time, current will be 10sec
                for(PlayerStats stats : gameHandler.getPlayerStats().values()) {
                    if(!stats.isAlive()) {
                        if(stats.getTimeSinceDeath() >= 10) {
                            gameHandler.respawnPlayer(stats.getPlayer());
                        } else if(stats.getTimeSinceDeath() % 10 == 0) {
                            int secondsUntilSpawn = 10 - stats.getTimeSinceDeath();
                            int minutesUntilSpawn = (int) TimeUnit.SECONDS.toMinutes(secondsUntilSpawn);
                            secondsUntilSpawn = secondsUntilSpawn % 60;
                            String message = "";
                            if(minutesUntilSpawn != 0) message += minutesUntilSpawn + "min ";
                            if(secondsUntilSpawn != 0) message += secondsUntilSpawn + "sec ";
                            if(!message.isEmpty()) {
                                stats.getPlayer().sendMessage(message);
                            }
                        }
                    }
                }
            }
            seconds++;
        }
    }

    public void setWave(int wave) {
        int previousWave = this.wave;
        this.wave = wave;

        /*
        Force the entire game to stop if the wave goes higher than the max wave

        TODO: Add a message that sends to all players
        Possibly add a title popup and an extra reward
         */
        if (this.wave > maxWave) {
            gameHandler.stop();
        }

        prepareNextWave();

        WaveChangeEvent event = new WaveChangeEvent(previousWave, wave, timeUntilNextWave);
        Bukkit.getPluginManager().callEvent(event);
        timeUntilNextWave = event.getSecondsUntilStart();
        this.wave = event.getNewWave();
    }

    public void start() {
        wave = 1;
        seconds = 0;
        entities.clear();
        Log.debug("Wave set to: " + wave + ". Preparing next wave...");
        prepareNextWave();
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZombieArena.getInstance(), this, 0L, 20L);
    }

    private void startWave() {
//        for(Player p : gameHandler.getPlayers()) {
//            // Utilize this if I want to update the players health to max on next wave
//            // Most likely don't see myself using this
//        }
        // Clear entities that were stuck or not killed last wave; can find a way to teleport them
        entities.clear();

        WaveStartEvent event = new WaveStartEvent(wave);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void stop() {
        if(taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }
    
    private void updateEntityList() {
        for (Map.Entry<Zombie, Integer> entity : entities.entrySet()) {
            Log.debug("Checking " + entity.getKey() + " and " + entity.getValue());
            if (!entity.getKey().isValid()) {
                Log.debug("Deleting " + entity.getKey() + " : " + entity.getValue());
                entities.remove(entity.getKey(), entity.getValue());
                break;
                /*
                Have to break early because if a mob is removed from the Map and
                it is trying to go through all of the Map it will go one value too far
                causing an error to be thrown; to prevent just break early
                 */
            }
        }
    }

}
