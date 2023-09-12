package me.Visionexe.ZombieArena.GUI.Blacksmith;

import me.Visionexe.ZombieArena.GUI.*;
import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.ZombieArena;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sword extends ChestGUI implements Clickable {

    private ItemStack swordItemStack = null;
    private int sharpnessMax = 20, smiteMax = 20, baneMax = 20, fireAspectMax = 5, knockbackMax = 3;
    private boolean isSharpnessMax = false, isSmiteMax = false, isBaneMax = false, isFireAspectMax = false, isKnockbackMax = false;
    private double sharpnessPrice, smitePrice, banePrice, fireAspectPrice, knockbackPrice;

    public Sword(String title, Row size) {
        super(title, size);
    }

    @Override
    public void beforeOpen(Player player) {
        /*
        Since the item is already checked before the player opens this GUI in Blacksmith Class
        There is no need to check if the item itself is available

        This will just need to do the following:
        - Get custom sword from inventory
        - Check enchantments on item
        - Use enchantments to place correct "next level" enchantment books in GUI
        - Allow player to purchase enchantment
         */
        ItemStack[] itemsInInventory = player.getInventory().getContents();
        ItemStack sharpnessEnchantmentItemStack = null, smiteEnchantmentItemStack = null, baneItemStack = null, fireAspectItemStack = null, knockbackItemStack = null;

        for (ItemStack itemStack : itemsInInventory) {
            if (itemStack != null) {
                if (itemStack.getItemMeta().getDisplayName().contains("Berserk Sword")) {
                    swordItemStack = itemStack;
                    break;
                }
            }
        }

        if (swordItemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= sharpnessMax) {
            isSharpnessMax = true;
            sharpnessEnchantmentItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eSharpness " + sharpnessMax,
                    isSharpnessMax
            );
            sharpnessEnchantmentItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpnessMax);
        }
        if (swordItemStack.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD) >= smiteMax) {
            isSmiteMax = true;
            smiteEnchantmentItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eSmite " + smiteMax,
                    isSmiteMax
            );
            smiteEnchantmentItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, smiteMax);
        }
        if (swordItemStack.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS) >= baneMax) {
            isBaneMax = true;
            baneItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eBane of Arthropods " + baneMax,
                    isBaneMax
            );
            baneItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, baneMax);
        }
        if (swordItemStack.getEnchantmentLevel(Enchantment.FIRE_ASPECT) >= fireAspectMax) {
            isFireAspectMax = true;
            fireAspectItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eFire Aspect " + fireAspectMax,
                    isFireAspectMax
            );
            fireAspectItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, fireAspectMax);
        }
        if (swordItemStack.getEnchantmentLevel(Enchantment.KNOCKBACK) >= knockbackMax) {
            isKnockbackMax = true;
            knockbackItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eKnockback " + knockbackMax,
                    isKnockbackMax
            );
            knockbackItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, knockbackMax);
        }
        Map<Enchantment, Integer> swordEnchantments = swordItemStack.getEnchantments();
        if (swordEnchantments.containsKey(Enchantment.DAMAGE_ALL) && !(isSharpnessMax)) {
            sharpnessEnchantmentItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eSharpness " + (swordEnchantments.get(Enchantment.DAMAGE_ALL) + 1),
                    sharpnessPrice = getEnchantPrice(swordEnchantments.get(Enchantment.DAMAGE_ALL) + 1)
            );
            sharpnessEnchantmentItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, swordEnchantments.get(Enchantment.DAMAGE_ALL) + 1);
            player.sendMessage(sharpnessEnchantmentItemStack.getItemMeta().getLore().toString());
        } else if (!(isSharpnessMax)) {
            sharpnessEnchantmentItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eSharpness 1",
                    sharpnessPrice = getEnchantPrice(1)
            );
            sharpnessEnchantmentItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        }

        if (swordEnchantments.containsKey(Enchantment.DAMAGE_UNDEAD) && !(isSmiteMax)) {
            smiteEnchantmentItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eSmite " + (swordEnchantments.get(Enchantment.DAMAGE_UNDEAD) + 1),
                    smitePrice = getEnchantPrice(swordEnchantments.get(Enchantment.DAMAGE_UNDEAD) + 1)

            );
            smiteEnchantmentItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, swordEnchantments.get(Enchantment.DAMAGE_UNDEAD) + 1);
        } else if (!(isSmiteMax)) {
            smiteEnchantmentItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eSmite 1",
                    smitePrice = getEnchantPrice(1)
            );
            smiteEnchantmentItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 1);
        }

        if (swordEnchantments.containsKey(Enchantment.DAMAGE_ARTHROPODS) && !(isBaneMax)) {
            baneItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eBane of Arthropods " + (swordEnchantments.get(Enchantment.DAMAGE_ARTHROPODS) + 1),
                    banePrice = getEnchantPrice(swordEnchantments.get(Enchantment.DAMAGE_ARTHROPODS) + 1)

            );
            baneItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, swordEnchantments.get(Enchantment.DAMAGE_ARTHROPODS) + 1);
        } else if (!(isBaneMax)) {
            baneItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eBane of Arthropods 1",
                    banePrice = getEnchantPrice(1)
            );
            baneItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 1);
        }

        if (swordEnchantments.containsKey(Enchantment.FIRE_ASPECT) && !(isFireAspectMax)) {
            fireAspectItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eFire Aspect " + (swordEnchantments.get(Enchantment.FIRE_ASPECT) + 1),
                    fireAspectPrice = getEnchantPrice(swordEnchantments.get(Enchantment.FIRE_ASPECT) + 1)
            );
            fireAspectItemStack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, swordEnchantments.get(Enchantment.FIRE_ASPECT) + 1);
        } else if (!(isFireAspectMax)) {
            fireAspectItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eFire Aspect 1",
                    fireAspectPrice = getEnchantPrice(1)
            );
            fireAspectItemStack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
        }

        if (swordEnchantments.containsKey(Enchantment.KNOCKBACK) && !(isKnockbackMax)) {
            knockbackItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eKnockback " + (swordEnchantments.get(Enchantment.KNOCKBACK) + 1),
                    knockbackPrice = getEnchantPrice(swordEnchantments.get(Enchantment.KNOCKBACK) + 1)
            );
            knockbackItemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, swordEnchantments.get(Enchantment.KNOCKBACK) + 1);
        } else if (!(isKnockbackMax)) {
            knockbackItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eKnockback 1",
                    knockbackPrice = getEnchantPrice(1)
            );
            knockbackItemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        }

        /*
        Add custom items as a GUIItem and then set them to their respective slots
         */
        GUIItem swordItem = new GUIItem(swordItemStack);
        GUIItem sharpnessItem = new GUIItem(sharpnessEnchantmentItemStack);
        GUIItem smiteItem = new GUIItem(smiteEnchantmentItemStack);
        GUIItem baneItem = new GUIItem(baneItemStack);
        GUIItem fireAspectItem = new GUIItem(fireAspectItemStack);
        GUIItem knockbackItem = new GUIItem(knockbackItemStack);

        set(Column.TWO, Row.TWO, swordItem);
        set(Column.FOUR, Row.TWO, sharpnessItem);
        set(Column.FIVE, Row.TWO, smiteItem);
        set(Column.SIX, Row.TWO, baneItem);
        set(Column.SEVEN, Row.TWO, fireAspectItem);
        set(Column.EIGHT, Row.TWO, knockbackItem);
    }

    /**
     * Creates an item stack of the given material. Messages are taken from the specified configuration section.
     */
    private ItemStack createItemStack(Material material, String displayName, Double price) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Buy: &a$") + price);
        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack createItemStack(Material material, String displayName, boolean isMaxed) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&eEnchantment Maxed"));
        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void onOpen(Player player) {

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

        Player player = (Player) event.getWhoClicked();
        Economy economy = ZombieArena.getInstance().getEconomy();

        int slot = event.getSlot();

        Log.debug("Slot clicked: " + slot);

        // Sharpness
        if (isSlot(slot, Column.FOUR, Row.TWO)) {
            if (!(isSharpnessMax)) {
                if (economy.getBalance(player) >= sharpnessPrice) {
                    economy.withdrawPlayer(player, sharpnessPrice);
                    swordItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, swordItemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) + 1);
                    player.sendMessage("Sharpness Enchant Upgraded!");
                    beforeOpen(player);
                    updateContent();
                } else {
                    player.sendMessage("You do not have enough money to purchase this!");
                    beforeOpen(player);
                    updateContent();
                }
            }
            beforeOpen(player);
            updateContent();
        }

        // Smite
        if (isSlot(slot, Column.FIVE, Row.TWO)) {
            if (!(isSmiteMax)) {
                if (economy.getBalance(player) >= smitePrice) {
                    economy.withdrawPlayer(player, smitePrice);
                    swordItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, swordItemStack.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD) + 1);
                    player.sendMessage("Smite Enchant Upgraded!");
                    beforeOpen(player);
                    updateContent();
                } else {
                    player.sendMessage("You do not have enough money to purchase this!");
                    beforeOpen(player);
                    updateContent();
                }
            }
            beforeOpen(player);
            updateContent();
        }

        // Bane
        if (isSlot(slot, Column.SIX, Row.TWO)) {
            if (!(isBaneMax)) {
                if (economy.getBalance(player) >= banePrice) {
                    economy.withdrawPlayer(player, banePrice);
                    swordItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, swordItemStack.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS) + 1);
                    player.sendMessage("Bane of Arthropods Enchant Upgraded!");
                    beforeOpen(player);
                    updateContent();
                } else {
                    player.sendMessage("You do not have enough money to purchase this!");
                    beforeOpen(player);
                    updateContent();
                }
            }
            beforeOpen(player);
            updateContent();
        }

        // Fire Aspect
        if (isSlot(slot, Column.SEVEN, Row.TWO)) {
            if (!(isFireAspectMax)) {
                if (economy.getBalance(player) >= fireAspectPrice) {
                    economy.withdrawPlayer(player, fireAspectPrice);
                    swordItemStack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, swordItemStack.getEnchantmentLevel(Enchantment.FIRE_ASPECT) + 1);
                    player.sendMessage("Fire Aspect Enchant Upgraded!");
                    beforeOpen(player);
                    updateContent();
                } else {
                    player.sendMessage("You do not have enough money to purchase this!");
                    beforeOpen(player);
                    updateContent();
                }
            }
            beforeOpen(player);
            updateContent();
        }

        // Knockback
        if (isSlot(slot, Column.EIGHT, Row.TWO)) {
            if (!(isKnockbackMax)) {
                if (economy.getBalance(player) >= knockbackPrice) {
                    economy.withdrawPlayer(player, knockbackPrice);
                    swordItemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, swordItemStack.getEnchantmentLevel(Enchantment.KNOCKBACK) + 1);
                    player.sendMessage("Knockback Enchant Upgraded!");
                    beforeOpen(player);
                    updateContent();
                } else {
                    player.sendMessage("You do not have enough money to purchase this!");
                    beforeOpen(player);
                    updateContent();
                }
            }
            beforeOpen(player);
            updateContent();
        }
    }

    public double getEnchantPrice(int enchantLevel) {
        return (Math.pow(2, (enchantLevel - 1))) * 500;
    }
}
