package me.Visionexe.ZombieArena.Listener;

import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobListener implements Listener {
    /*
    This will create a generic case to detect whether an entity was killed by a player
    If entity is not killed by a player, return and do nothing
    If entity is killed by the player, obtain reward from config and apply to player

    Example:
    Zombie is killed by player
    Cancel all types of events (drops, xp gain, etc.)
    Check the reward from config (xp gain, coin gain)
    Apply reward to player

    Need to make this generic enough to apply <MobType> to single listener
     */

    /*
    Mobs tiered in this order
    1. Zombie
    2. Skeleton
    3. Spider
    4. Creeper
    5. Piglin Brute
    6. Zoglin
    7. Blaze
    8. Wither Skeleton
    9. Wither (BOSS)
    10. Warden (BOSS)
     */

    /*
    These mobs will have a basic reward system that is multiplied by the difficulty type of the arena
    NOTE: Will need to create an ENUM for Arena Difficulties (will most likely be: Easy, Medium, Hard, Insane)
    Rewards will be
    1x for Easy
    2x for Medium
    3x for Hard
    4x for Insane
    Due to the way the rewards work, mob stats will also work the same way
    In order to scale optimally, will need to do dmg test with the enchants in mind
    Copying from ZombieV, Smite/Sharp 15 should be max
    Insane difficulty should never allow a player to 1 hit, 2 hits minimum to kill mobs with max enchants
     */

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // TODO: Figure out how to store these in a list with values of xp and coins
        // TODO: onDeath, check list, if mobtype is listed, give player appropriate amount of coins
        // TODO: if not listed, do nothing
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            PlayerWrapper playerWrapper = PlayerWrapper.get(player);
            if (event.getEntity() instanceof Zombie) {
                // Get zombie-coins and zombie-xp from settings
                // TODO: Get the game difficulty type and multiply by this value
                playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("zombie-xp"));
            } else if (event.getEntity() instanceof Skeleton) {
                playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("skeleton-xp"));
            } else if (event.getEntity() instanceof Spider) {
                playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("spider-xp"));
            } else if (event.getEntity() instanceof Creeper) {
                playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("creeper-xp"));
            } else if (event.getEntity() instanceof PiglinBrute) {
                playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("piglinbrute-xp"));
            } else if (event.getEntity() instanceof Zoglin) {
                playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("zoglin-xp"));
            } else if (event.getEntity() instanceof Blaze) {
                playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("blaze-xp"));
            } else if (event.getEntity() instanceof WitherSkeleton) {
                playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("witherskeleton-xp"));
            } else if (event.getEntity() instanceof Wither) {
                playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("wither-xp"));
            } else if (event.getEntity() instanceof Warden) {
                playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("warden-xp"));
            }
        }
    }
}
