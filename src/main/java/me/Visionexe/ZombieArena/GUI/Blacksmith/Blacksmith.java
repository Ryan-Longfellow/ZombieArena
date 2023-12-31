package me.Visionexe.ZombieArena.GUI.Blacksmith;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import me.Visionexe.ZombieArena.GUI.*;
import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.ZombieArena;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Blacksmith extends ChestGUI implements Clickable {

    private boolean hasSword, hasHelmet, hasChestplate, hasLeggings, hasBoots = false;
    public Blacksmith(String title, Row size) {
        super(title, size);
    }

    @Override
    public void beforeOpen(Player player) {
        Log.debug("Before open...");

        /*
        Get the player who is opening to access their inventory and check for custom item
        If they do not have the custom item, display lore to buy custom item

        If they do have the custom item, display a message to click to enchant
        When clicking open the new GUI with the single item as well as enchant books the
        player can click to level up the enchants on their item
         */

        // Get list of Mythic Items as well as all the Items in the Player's Inventory
        Collection<MythicItem> customItems = MythicBukkit.inst().getItemManager().getItems();
        ItemStack[] itemsInInventory = player.getInventory().getContents();
        List<String> itemsInInventoryString = new ArrayList<>();
        for (ItemStack itemStack : itemsInInventory) {
            if (itemStack != null) {
                itemsInInventoryString.add(itemStack.getItemMeta().getDisplayName());
            }
        }

        List<String> swordLore = new ArrayList<>();
        List<String> helmetLore = new ArrayList<>();
        List<String> chestplateLore = new ArrayList<>();
        List<String> leggingsLore = new ArrayList<>();
        List<String> bootsLore = new ArrayList<>();

        /*
        An individual lore list is needed for reach item
        If this is not done individually, this overlaps lore and causes additional or incorrect info
         */

        // Loop through list of Mythic Items to compare to Player's Inventory
        for (MythicItem customItem : customItems) {
            String customItemDisplay = customItem.getDisplayName();
            // If the player has the custom item in their inventory
            if (itemsInInventoryString.contains(customItem.getDisplayName())) {
                // Set lore to click on item, allow player to click and open respective inventory
                if (customItemDisplay.contains("Sword")) {
                    swordLore.add("&7Click to view/purchase enchantments.");
                    hasSword = true;
                } else if (customItemDisplay.contains("Helmet")) {
                    helmetLore.add("&7Click to view/purchase enchantments.");
                    hasHelmet = true;
                } else if (customItemDisplay.contains("Chestplate")) {
                    chestplateLore.add("&7Click to view/purchase enchantments.");
                    hasChestplate = true;
                } else if (customItemDisplay.contains("Leggings")) {
                    leggingsLore.add("&7Click to view/purchase enchantments.");
                    hasLeggings = true;
                } else if (customItemDisplay.contains("Boots")) {
                    bootsLore.add("&7Click to view/purchase enchantments.");
                    hasBoots = true;
                }
            } else {
                // Set lore for player to buy item if they click on it
                if (customItemDisplay.contains("Sword")) {
                    swordLore.add("&7It looks like you do not have this item.");
                    swordLore.add("&7Click to purchase for &a$10,000&7.");
                    hasSword = false;
                } else if (customItemDisplay.contains("Helmet")) {
                    helmetLore.add("&7It looks like you do not have this item.");
                    helmetLore.add("&7Click to purchase for &a$10,000&7.");
                    hasHelmet = false;
                } else if (customItemDisplay.contains("Chestplate")) {
                    chestplateLore.add("&7It looks like you do not have this item.");
                    chestplateLore.add("&7Click to purchase for &a$10,000&7.");
                    hasChestplate = false;
                } else if (customItemDisplay.contains("Leggings")) {
                    leggingsLore.add("&7It looks like you do not have this item.");
                    leggingsLore.add("&7Click to purchase for &a$10,000&7.");
                    hasLeggings = false;
                } else if (customItemDisplay.contains("Boots")) {
                    bootsLore.add("&7It looks like you do not have this item.");
                    bootsLore.add("&7Click to purchase for &a$10,000&7.");
                    hasBoots = false;
                }
            }
        }

        /*
        Need to create an item stack for
        - Berserk Sword
        - Custom Helmet
        - Custom Chestplate
        - Custom Leggings
        - Custom Boots
         */

        ItemStack swordItemStack = createItemStack(
                Material.NETHERITE_SWORD,
                "&4Berserk Sword",
                swordLore
        );
        ItemStack helmetItemStack = createItemStack(
                Material.NETHERITE_HELMET,
                "&4Netherite Helmet",
                helmetLore
        );
        ItemStack chestplateItemStack = createItemStack(
                Material.NETHERITE_CHESTPLATE,
                "&4Netherite Chestplate",
                chestplateLore
        );
        ItemStack leggingsItemStack = createItemStack(
                Material.NETHERITE_LEGGINGS,
                "&4Netherite Leggings",
                leggingsLore
        );
        ItemStack bootItemStack = createItemStack(
                Material.NETHERITE_BOOTS,
                "&4Netherite Boots",
                bootsLore
        );

        /*
        Add custom items as a GUIItem and then set them to their respective slots
         */
        GUIItem swordItem = new GUIItem(swordItemStack);
        GUIItem helmetItem = new GUIItem(helmetItemStack);
        GUIItem chestplateItem = new GUIItem(chestplateItemStack);
        GUIItem leggingsItem = new GUIItem(leggingsItemStack);
        GUIItem bootsItem = new GUIItem(bootItemStack);

        set(Column.THREE, Row.TWO, swordItem);
        set(Column.FOUR, Row.TWO, helmetItem);
        set(Column.FIVE, Row.TWO, chestplateItem);
        set(Column.SIX, Row.TWO, leggingsItem);
        set(Column.SEVEN, Row.TWO, bootsItem);
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
    public void onOpen(Player player) {
        // Not implemented, as it's not needed currently.
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Log.debug("On click...");
        if (event.getCurrentItem() == null)
            return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player)event.getWhoClicked();
        Economy economy = ZombieArena.getInstance().getEconomy();

        int slot = event.getSlot();

        Log.debug("Slot clicked: " + slot);

        // Sword Enchant Menu
        if (isSlot(slot, Column.THREE, Row.TWO)) {
            if (hasSword) {
                Sword gui = new Sword(ChatColor.translateAlternateColorCodes('&', "&aBlacksmith"), Row.THREE);
                gui.open(player);
            } else {
                beforeOpen(player);
                updateContent();

                // Get players current money
                double playerBalance = economy.getBalance(player);
                // Compare to price of sword ($10,000 coins currently)
                if (playerBalance >= 10000) {
                    // If player has enough, purchase
                    player.getInventory().addItem(MythicBukkit.inst().getItemManager().getItemStack("BerserkSword"));
                    economy.withdrawPlayer(player, 10000);
                    player.sendMessage("You have successfully purchased the Berserk Sword!");
                } else {
                    // If not, send player a message that they do not have enough money
                    player.sendMessage("You do not have enough money to purchase this item!");
                }
            }
            beforeOpen(player);
            updateContent();
        }

        // Helmet Enchant Menu
        if (isSlot(slot, Column.FOUR, Row.TWO)) {
            if (hasHelmet) {
                Armor gui = new Armor(ChatColor.translateAlternateColorCodes('&', "&aBlacksmith"), Row.THREE, "Helmet");
                gui.open(player);
            } else {
                beforeOpen(player);
                updateContent();

                // Get players current money
                double playerBalance = economy.getBalance(player);
                // Compare to price of sword ($10,000 coins currently)
                if (playerBalance >= 10000) {
                    // If player has enough, purchase
                    player.getInventory().addItem(MythicBukkit.inst().getItemManager().getItemStack("CustomHelmet"));
                    economy.withdrawPlayer(player, 10000);
                    player.sendMessage("You have successfully purchased the Netherite Helmet!");
                } else {
                    // If not, send player a message that they do not have enough money
                    player.sendMessage("You do not have enough money to purchase this item!");
                }
            }
            beforeOpen(player);
            updateContent();
        }

        // Chestplate Enchant Menu
        if (isSlot(slot, Column.FIVE, Row.TWO)) {
            if (hasChestplate) {
                Armor gui = new Armor(ChatColor.translateAlternateColorCodes('&', "&aBlacksmith"), Row.THREE, "Chestplate");
                gui.open(player);
            } else {
                beforeOpen(player);
                updateContent();

                // Get players current money
                double playerBalance = economy.getBalance(player);
                // Compare to price of sword ($10,000 coins currently)
                if (playerBalance >= 10000) {
                    // If player has enough, purchase
                    player.getInventory().addItem(MythicBukkit.inst().getItemManager().getItemStack("CustomChestplate"));
                    economy.withdrawPlayer(player, 10000);
                    player.sendMessage("You have successfully purchased the Netherite Chestplate!");
                } else {
                    // If not, send player a message that they do not have enough money
                    player.sendMessage("You do not have enough money to purchase this item!");
                }
            }
            beforeOpen(player);
            updateContent();
        }

        // Leggings Enchant Menu
        if (isSlot(slot, Column.SIX, Row.TWO)) {
            if (hasLeggings) {
                Armor gui = new Armor(ChatColor.translateAlternateColorCodes('&', "&aBlacksmith"), Row.THREE, "Leggings");
                gui.open(player);
            } else {
                beforeOpen(player);
                updateContent();

                // Get players current money
                double playerBalance = economy.getBalance(player);
                // Compare to price of sword ($10,000 coins currently)
                if (playerBalance >= 10000) {
                    // If player has enough, purchase
                    player.getInventory().addItem(MythicBukkit.inst().getItemManager().getItemStack("CustomLeggings"));
                    economy.withdrawPlayer(player, 10000);
                    player.sendMessage("You have successfully purchased the Netherite Leggings!");
                } else {
                    // If not, send player a message that they do not have enough money
                    player.sendMessage("You do not have enough money to purchase this item!");
                }
            }
            beforeOpen(player);
            updateContent();
        }

        // Boots Enchant Menu
        if (isSlot(slot, Column.SEVEN, Row.TWO)) {
            if (hasBoots) {
                Armor gui = new Armor(ChatColor.translateAlternateColorCodes('&', "&aBlacksmith"), Row.THREE, "Boots");
                gui.open(player);
            } else {
                beforeOpen(player);
                updateContent();

                // Get players current money
                double playerBalance = economy.getBalance(player);
                // Compare to price of sword ($10,000 coins currently)
                if (playerBalance >= 10000) {
                    // If player has enough, purchase
                    player.getInventory().addItem(MythicBukkit.inst().getItemManager().getItemStack("CustomBoots"));
                    economy.withdrawPlayer(player, 10000);
                    player.sendMessage("You have successfully purchased the Netherite Boots!");
                } else {
                    // If not, send player a message that they do not have enough money
                    player.sendMessage("You do not have enough money to purchase this item!");
                }
            }
            beforeOpen(player);
            updateContent();
        }
    }

}
