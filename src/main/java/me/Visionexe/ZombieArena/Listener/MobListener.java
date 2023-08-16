package me.Visionexe.ZombieArena.Listener;

import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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
        if (event.getEntity().getKiller() != null) {
            // Cancel normal mob dropping before performing anything else
            event.getDrops().clear();
            event.setDroppedExp(0);

            Player player = event.getEntity().getKiller();
            PlayerWrapper playerWrapper = PlayerWrapper.get(player);

            // Get the list of mobtypes in the config, allows easy editing so mob types are not hard coded
            ConfigurationSection mobTypes = ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getConfigurationSection("mob-xp");
            // Check if mob type is valid, if not return nothing
            for (String entity : mobTypes.getKeys(false)) {
                try {
                    if (event.getEntityType() == EntityType.valueOf(entity.toUpperCase())) {
                        playerWrapper.addExperience(ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getInt("mob-xp." + entity.toLowerCase()));

                    }
                } catch (Exception exception) {
                    Bukkit.getConsoleSender().sendMessage("Invalid ENTITY TYPE entered in config. Please review link in config to obtain proper ENTITY TYPE.");
                }
            }
        }
    }
}
