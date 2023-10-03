package me.Visionexe.ZombieArena.GUI.Game;

import me.Visionexe.ZombieArena.Entity.PlayerWrapper;
import me.Visionexe.ZombieArena.GUI.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Stats extends ChestGUI implements Clickable {
    Player player;
    public Stats(String title, Row size, Player player) {
        super(title, size);
        this.player = player;
    }

    @Override
    public void beforeOpen(Player player) {
        /*
        Split GUI into 3 different sections
        PLayer Stats - displays level, exp, games played, games won, total kills, total boss damage dealt
        Mob Stats - display each individual mob kill
        Boss Stats - displays each boss kill and boss damage
         */
        PlayerWrapper playerWrapper = PlayerWrapper.get(player);
        List<String> playerStatsLore = new ArrayList<>();
        List<String> mobStatsLore = new ArrayList<>();
        List<String> bossStatsLore = new ArrayList<>();

        // Add all player stats information to lore
        playerStatsLore.add("&6Level&f: " + playerWrapper.getLevel());
        playerStatsLore.add("&2Exp&f: " + playerWrapper.getExperience() + " / " + playerWrapper.getExperienceForNextLevel());
        playerStatsLore.add("&bPrestige&f: " + playerWrapper.getPrestige());
        playerStatsLore.add("&eGames Played&f: " + playerWrapper.getGamesPlayed());
        playerStatsLore.add("&aGames Won&f: " + playerWrapper.getGamesWon());
        playerStatsLore.add("&cTotal Kills&f: " + playerWrapper.getTotalKills());
        playerStatsLore.add("&3Total Boss Damage&f: " + playerWrapper.getTotalBossDamage());

        // Add all mob stats information to lore
        mobStatsLore.add("Zombie Kills&f: " + playerWrapper.getZombieKills());
        mobStatsLore.add("Skeleton Kills&f: " + playerWrapper.getSkeletonKills());
        mobStatsLore.add("Spider Kills&f: " + playerWrapper.getSpiderKills());
        mobStatsLore.add("Piglin Brute Kills&f: " + playerWrapper.getPiglinBruteKills());
        mobStatsLore.add("Zoglin Kills&f: " + playerWrapper.getZoglinKills());
        mobStatsLore.add("Blaze Kills&f: " + playerWrapper.getBlazeKills());
        mobStatsLore.add("Wither Skeleton Kills&f: " + playerWrapper.getWitherSkeletonKills());

        // Add all boss stats information to lore
        bossStatsLore.add("Wave 10 Boss Kills&f: " + playerWrapper.getWave10BossKills());
        bossStatsLore.add("Wave 20 Boss Kills&f: " + playerWrapper.getWave20BossKills());
        bossStatsLore.add("Wave 30 Boss Kills&f: " + playerWrapper.getWave30BossKills());
        bossStatsLore.add("Wave 40 Boss Kills&f: " + playerWrapper.getWave40BossKills());
        bossStatsLore.add("Wave 50 Boss Kills&f: " + playerWrapper.getWave50BossKills());
        bossStatsLore.add("");
        bossStatsLore.add("Wave 10 Boss Damage&f: " + playerWrapper.getWave10BossDamage());
        bossStatsLore.add("Wave 20 Boss Damage&f: " + playerWrapper.getWave20BossDamage());
        bossStatsLore.add("Wave 30 Boss Damage&f: " + playerWrapper.getWave30BossDamage());
        bossStatsLore.add("Wave 40 Boss Damage&f: " + playerWrapper.getWave40BossDamage());
        bossStatsLore.add("Wave 50 Boss Damage&f: " + playerWrapper.getWave50BossDamage());

        // Create ItemStack for each section
        ItemStack playerStatsItemStack = createItemStack(
                Material.PLAYER_HEAD,
                "&bPlayer Stats",
                playerStatsLore
        );
        SkullMeta playerHead = (SkullMeta) playerStatsItemStack.getItemMeta();
        Objects.requireNonNull(playerHead).setOwningPlayer(player);
        playerStatsItemStack.setItemMeta(playerHead);

        ItemStack mobStatsItemStack = createItemStack(
                Material.ZOMBIE_HEAD,
                "&bMob Stats",
                mobStatsLore
        );
        ItemStack bossStatsItemStack = createItemStack(
                Material.WITHER_SKELETON_SKULL,
                "&bBoss Stats",
                bossStatsLore
        );

        // Create and set the GUI Items
        GUIItem playerStatsItem = new GUIItem(playerStatsItemStack);
        GUIItem mobStatsItem = new GUIItem(mobStatsItemStack);
        GUIItem bossStatsItem = new GUIItem(bossStatsItemStack);

        set(Column.THREE, Row.TWO, playerStatsItem);
        set(Column.FIVE, Row.TWO, mobStatsItem);
        set(Column.SEVEN, Row.TWO, bossStatsItem);
    }

    /**
     * Creates an item stack of the given material. Messages are taken from the specified configuration section.
     */
    private ItemStack createItemStack(Material material, String displayName, List<String> loreLines) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        List<String> lore = new ArrayList<>();

        for (String line : loreLines) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }

    @Override
    public void onOpen(Player player) {

    }
}
