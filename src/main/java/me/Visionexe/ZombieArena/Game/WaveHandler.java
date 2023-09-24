package me.Visionexe.ZombieArena.Game;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Visionexe.ZombieArena.Event.GameStopCause;
import me.Visionexe.ZombieArena.Event.GameStopEvent;
import me.Visionexe.ZombieArena.Event.WaveChangeEvent;
import me.Visionexe.ZombieArena.Event.WaveStartEvent;
import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
    private Map<ActiveMob, Integer> entities;

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
        List<Player> playersInGame = gameHandler.getPlayers();
        // A very bad process of getting the mobSpawns of an arena by taking the first player listed in the arena and getting the arena name from them
        List<Location> mobSpawns = arenaHandler.getMobSpawns(gameHandler.getPlayerStats(gameHandler.getPlayers().get(0)).getArenaName());
        List<Location> playerLocations = new ArrayList<>();
        List<Location> spawnsCloseToPlayers = new ArrayList<>();

        for (Player player : playersInGame) {
            playerLocations.add(player.getLocation());
        }
        for (Location spawn : mobSpawns) {
            for (Location playerLocation : playerLocations) {
                if (spawn.distance(playerLocation) <= 20) {
                    spawnsCloseToPlayers.add(spawn);
                }
            }
        }
        // mobSpawns.get(random.nextInt(mobSpawns.size()))
        ActiveMob activeMob = getRandomMobToSpawn().spawn(BukkitAdapter.adapt(spawnsCloseToPlayers.get(random.nextInt(spawnsCloseToPlayers.size()))), 1);
        mobsToSpawn--;
        entities.put(activeMob, activeMob.getEntity().getBukkitEntity().getEntityId());
    }

    private MythicMob getRandomMobToSpawn() {
        int wave = getWave();
        int chance = (random.nextInt(100) + 1); // Chance to spawn a mob that isn't a zombie
        int mobChance; // Which mob type to choose to spawn

        // Perform all boss checks first
        if (wave == 10) {
            mobsToSpawn = 1;
            return MythicBukkit.inst().getMobManager().getMythicMob("ZombieBoss").orElse(null);
        }
        if (wave == 20) {
            mobsToSpawn = 1;
            return MythicBukkit.inst().getMobManager().getMythicMob("PiglinBoss").orElse(null);
        }
        if (wave == 30) {
            mobsToSpawn = 1;
            return MythicBukkit.inst().getMobManager().getMythicMob("BlazeBoss").orElse(null);
        }
        if (wave == 40) {
            mobsToSpawn = 1;
            return MythicBukkit.inst().getMobManager().getMythicMob("WitherSkeletonBoss").orElse(null);
        }
        if (wave == 50) {
            mobsToSpawn = 1;
            return MythicBukkit.inst().getMobManager().getMythicMob("WardenBoss").orElse(null);
        }

        // Mob that is not a zombie is spawning
        if (chance <= 10) {
            if (wave < 50) {
                if (wave < 40) {
                    if (wave < 30) {
                        if (wave < 20) {
                            if (wave > 5 && wave < 10) {
                                return MythicBukkit.inst().getMobManager().getMythicMob("BaseSkeleton").orElse(null);
                            }
                            if (wave < 5) {
                                return MythicBukkit.inst().getMobManager().getMythicMob("BaseZombie").orElse(null);
                            }
                            mobChance = (random.nextInt(2) + 1);
                            switch (mobChance) {
                                case 1 -> {
                                    return MythicBukkit.inst().getMobManager().getMythicMob("BaseSkeleton").orElse(null);
                                }
                                case 2 -> {
                                    return MythicBukkit.inst().getMobManager().getMythicMob("BaseSpider").orElse(null);
                                }
                            }
                        }
                        mobChance = (random.nextInt(3) + 1);
                        switch (mobChance) {
                            case 1 -> {
                                return MythicBukkit.inst().getMobManager().getMythicMob("BaseSkeleton").orElse(null);
                            }
                            case 2 -> {
                                return MythicBukkit.inst().getMobManager().getMythicMob("BaseSpider").orElse(null);
                            }
                            case 3 -> {
                                return MythicBukkit.inst().getMobManager().getMythicMob("BasePiglinBrute").orElse(null);
                            }
                        }
                    }
                    mobChance = (random.nextInt(4) + 1);
                    switch (mobChance) {
                        case 1 -> {
                            return MythicBukkit.inst().getMobManager().getMythicMob("BaseSkeleton").orElse(null);
                        }
                        case 2 -> {
                            return MythicBukkit.inst().getMobManager().getMythicMob("BaseSpider").orElse(null);
                        }
                        case 3 -> {
                            return MythicBukkit.inst().getMobManager().getMythicMob("BasePiglinBrute").orElse(null);
                        }
                        case 4 -> {
                            return MythicBukkit.inst().getMobManager().getMythicMob("BaseZoglin").orElse(null);
                        }
                    }
                }
                mobChance = (random.nextInt(6) + 1);
                switch (mobChance) {
                    case 1 -> {
                        return MythicBukkit.inst().getMobManager().getMythicMob("BaseSkeleton").orElse(null);
                    }
                    case 2 -> {
                        return MythicBukkit.inst().getMobManager().getMythicMob("BaseSpider").orElse(null);
                    }
                    case 3 -> {
                        return MythicBukkit.inst().getMobManager().getMythicMob("BasePiglinBrute").orElse(null);
                    }
                    case 4 -> {
                        return MythicBukkit.inst().getMobManager().getMythicMob("BaseZoglin").orElse(null);
                    }
                    case 5 -> {
                        return MythicBukkit.inst().getMobManager().getMythicMob("BaseBlaze").orElse(null);
                    }
                    case 6 -> {
                        return MythicBukkit.inst().getMobManager().getMythicMob("BaseWitherSkeleton").orElse(null);
                    }
                }
            }
        }
        return MythicBukkit.inst().getMobManager().getMythicMob("BaseZombie").orElse(null);
    }

    private boolean checkNextWave() {
        return mobsToSpawn <= 0 && entities.isEmpty() && timeUntilNextWave <= 0;
    }

    public int getGameLength() { return seconds; }

    public int getRemainingZombies() {
        return entities.size() + mobsToSpawn;
    }

    public int getWave() { return wave; }
    public int getMaxWave() {return maxWave; }

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
        return wave * 3; // Currently just returning the wave * 5 to spawn that many entities; will create something more complex later maybe
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
        if (gameHandler.isRunning()) {
            if(timeUntilNextWave > 0) {
                timeUntilNextWave--;
                if(timeUntilNextWave <= 0) {
                    startWave();
                }
            } else {
                /*
                Runs 5 times every second to spawn mob and check if next wave is available
                Will start the next wave is all criteria is met
                 */
                for (int i = 0; i <= mobsToSpawn; i++) {
                    if (mobsToSpawn <= 0) { break; }
                    attemptSpawnEntity();
                }

                for (int i = 0; i < 10; i++) {
                    updateEntityList();
                }

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
                    if(!(stats.isAlive())) {
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

            GameStopEvent event = new GameStopEvent(GameStopCause.WIN);
            Bukkit.getPluginManager().callEvent(event);
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
        for (Map.Entry<ActiveMob, Integer> entity : entities.entrySet()) {
//            Log.debug("Checking " + entity.getKey() + " and " + entity.getValue());
            if (!entity.getKey().getEntity().getBukkitEntity().isValid()) {
//                Log.debug("Deleting " + entity.getKey() + " : " + entity.getValue());
                entities.remove(entity.getKey(), entity.getValue());
                break;
                /*
                Have to break early because if a mob is removed from the Map and
                it is trying to go through all the Map it will go one value too far
                causing an error to be thrown; to prevent just break early
                 */
            }
        }
    }

    public void clearEntityList() {
        entities.clear();
    }

    public void removeEntities() {
        if (entities.isEmpty()) return;
        for (Map.Entry<ActiveMob, Integer> entity : entities.entrySet()) {
            if (!entity.getKey().getEntity().getBukkitEntity().isValid()) {
                entities.remove(entity.getKey(), entity.getValue());
            } else {
                entity.getKey().remove();
            }
        }
        entities.clear();
    }
}
