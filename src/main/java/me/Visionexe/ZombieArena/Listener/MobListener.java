package me.Visionexe.ZombieArena.Listener;

import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.ZombieArena;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.*;
import java.util.stream.Collectors;

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

    private Configuration config = ZombieArena.getInstance().getFileManager().get("config").get().getConfiguration();
    Economy economy = ZombieArena.getInstance().getEconomy();
    private Map<Player, Double> topDamage = new HashMap<>();

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            // Cancel normal mob dropping before performing anything else
            event.getDrops().clear();
            event.setDroppedExp(0);

            Player player = event.getEntity().getKiller();
            PlayerWrapper playerWrapper = PlayerWrapper.get(player);

            if (event.getEntity().getName().contains("BOSS")) {
                LinkedHashMap<Player, Double> sortedTopDamage = (LinkedHashMap<Player, Double>) sortDamagers(topDamage);
                int count = 0;
                Bukkit.broadcastMessage(ChatColor.GREEN + "-----------------------------------------");
                for (Map.Entry<Player, Double> damager : sortedTopDamage.entrySet()) {
                    count++;
                    if (count >= 5) break;

                    /*
                    Broadcast message as follows
                    -----------------------------------------
                    WAVE <#> BOSS KILLED
                    1. Damager
                    2. Damager
                    3. Damager
                    4. Damager
                    5. Damager
                    All other players received 10% of rewards
                    -----------------------------------------
                     */
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "" + count + ". " +
                            ChatColor.WHITE + damager.getKey().getName() + " dealt " +
                            ChatColor.AQUA + damager.getValue() +
                            ChatColor.WHITE + " damage");
                }
                Bukkit.broadcastMessage(ChatColor.GREEN + "-----------------------------------------");


                // Check size of top damagers
                // If <= 5 damagers, split equally among all damagers
                // If > 5 damagers, split among top 5, give all other players 5%

                switch (event.getEntityType()) {
                    case ZOMBIE -> {
                        splitRewards(sortedTopDamage,
                                config.getInt("boss-types.zombie.xp"),
                                config.getDouble("boss-types.zombie.coins"));
                        topDamage.clear();
                    }
                    case PIGLIN_BRUTE -> {
                        splitRewards(sortedTopDamage,
                                config.getInt("boss-types.piglin-brute.xp"),
                                config.getDouble("boss-types.piglin-brute.coins"));
                        topDamage.clear();
                    }
                    case BLAZE -> {
                        splitRewards(sortedTopDamage,
                                config.getInt("boss-types.blaze.xp"),
                                config.getDouble("boss-types.blaze.coins"));
                        topDamage.clear();
                    }
                    case WITHER_SKELETON -> {
                        splitRewards(sortedTopDamage,
                                config.getInt("boss-types.wither_skeleton.xp"),
                                config.getDouble("boss-types.wither_skeleton.coins"));
                        topDamage.clear();
                    }
                    case WARDEN -> {
                        splitRewards(sortedTopDamage,
                                config.getInt("boss-types.warden.xp"),
                                config.getDouble("boss-types.warden.coins"));
                        topDamage.clear();
                    }
                }
                return;
            }

            // Get the list of mobtypes in the config, allows easy editing so mob types are not hard coded
            ConfigurationSection mobTypes = config.getConfigurationSection("mob-types");
            // Check if mob type is valid, if not return nothing
            try {
                for (String entity : mobTypes.getKeys(false)) {
                        if (event.getEntityType() == EntityType.valueOf(entity.toUpperCase())) {
                            playerWrapper.addExperience(config.getInt("mob-types." + entity.toLowerCase() + ".xp"));
                            economy.depositPlayer(player, config.getInt("mob-types." + entity.toLowerCase() + ".coins"));
                        }
                }
            } catch (Exception exception) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + exception.getMessage());
            }
        }
    }

    /*
    This entire section will handle the top 5 damager for bosses and split rewards for each boss
    Rewards for bosses will be hard coded for now until exact boss names and information are
    figured out so a config value can be added

    Will need to also implement something in the onEntityDeath to register if the mob was a boss as well and provide
    rewards to the top 5 damagers
     */

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        // Cancels action if player is not the damager
        if (!(damager instanceof Player)) {
            return;
        }
        // Detects if the entity has a name containing BOSS, all bosses will be labeled this way
        if (entity.getName().contains("BOSS")) {
            // Cast damager into Player object to prevent having to cast each time
            Player playerDamager = (Player) damager;

            // Get the current damage dealt to be used to add to damage already done or put new player into list
            double currentDamage = event.getDamage();

            // If the list is brand new or does not contain the player, add player to list
            if (!(topDamage.containsKey(playerDamager))) {
                topDamage.put(playerDamager, currentDamage);
            }
            // If player is already on the damage list, add their current damage to their damage from the list
            if (topDamage.containsKey(playerDamager)) {
                double totalDamage;
                totalDamage = topDamage.get(playerDamager) + currentDamage;
                topDamage.replace(playerDamager, totalDamage);
            }
        }
    }

    /*
    This is solely used to sort damages by most damage done to least damage done for bosses
     */
    private Map<Player, Double> sortDamagers(Map<Player, Double> toSort) {
        return toSort.entrySet().stream()
                .sorted(Map.Entry.<Player, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private void splitRewards(LinkedHashMap<Player, Double> damagers, int experience, double money) {
        int totalDamagers = damagers.size();
        if (totalDamagers <= 5) {
            for (Map.Entry<Player, Double> damager : damagers.entrySet()) {
                PlayerWrapper.get(damager.getKey()).addExperience(experience / totalDamagers);
                economy.depositPlayer(damager.getKey(), money / totalDamagers);
                damager.getKey().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aYou received &e" + (experience / totalDamagers) + " &aexperience and &e$" +
                        (money / totalDamagers) + "&a."));
            }
        } else {
            int count = 0;
            for (Map.Entry<Player, Double> damager : damagers.entrySet()) {
                count++;
                if (count <= 5) {
                    PlayerWrapper.get(damager.getKey()).addExperience(experience / 5);
                    economy.depositPlayer(damager.getKey(), money / 5);
                    damager.getKey().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aYou received &e" + (experience / 5) + " &aexperience and &e$" +
                                    (money / 5) + "&a."));
                } else {
                    PlayerWrapper.get(damager.getKey()).addExperience((int) (experience * 0.10));
                    economy.depositPlayer(damager.getKey(), money * 0.10);
//                    "You received " + (experience * 0.10) + " experience and " +
//                            (money * 0.10) + " money"
                    damager.getKey().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aYou received &e" + (experience * 0.10) + " &aexperience and " +
                                    (money * 0.10) + "&a."));
                }
            }
        }
    }
}
