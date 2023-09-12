package me.Visionexe.ZombieArena.GUI.Blacksmith;

import me.Visionexe.ZombieArena.GUI.*;
import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.Utils.ValueFormat;
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

public class Armor extends ChestGUI implements Clickable {
    String armorType;
    private ItemStack armorItemStack = null;
    private int protectionMax = 20, projectileProjectionMax = 20, fireProtectionMax = 20, blastProtectionMax = 20, thornsMax = 10;
    private boolean isProtectionMax = false, isProjectileProtectionMax = false, isFireProtectionMax = false, isBlastProtectionMax = false, isThornsMax = false;
    private long protectionPrice, projectionProtectionPrice, fireProtectionPrice, blastProtectionPrice, thornsPrice;

    public Armor(String title, Row size, String armorType) {
        super(title, size);
        this.armorType = armorType;
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
        ItemStack protectionItemStack = null, projectileProtectionItemStack = null, fireProtectionItemStack = null, blastProtectionItemStack = null, thornsItemStack = null;

        for (ItemStack itemStack : itemsInInventory) {
            if (itemStack != null) {
                if (itemStack.getItemMeta().getDisplayName().contains("Netherite " + armorType)) {
                    armorItemStack = itemStack;
                    break;
                }
            }
        }

        if (armorItemStack.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) >= protectionMax) {
            isProtectionMax = true;
            protectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eProtection " + protectionMax,
                    isProtectionMax
            );
            protectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protectionMax);
        }
        if (armorItemStack.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE) >= projectileProjectionMax) {
            isProjectileProtectionMax = true;
            projectileProtectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eProjectile Protection " + projectileProjectionMax,
                    isProjectileProtectionMax
            );
            projectileProtectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, projectileProjectionMax);
        }
        if (armorItemStack.getEnchantmentLevel(Enchantment.PROTECTION_FIRE) >= fireProtectionMax) {
            isFireProtectionMax = true;
            fireProtectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eFire Protection " + fireProtectionMax,
                    isFireProtectionMax
            );
            fireProtectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, fireProtectionMax);
        }
        if (armorItemStack.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS) >= blastProtectionMax) {
            isBlastProtectionMax = true;
            blastProtectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eBlast Protection " + blastProtectionMax,
                    isBlastProtectionMax
            );
            blastProtectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, blastProtectionMax);
        }
        if (armorItemStack.getEnchantmentLevel(Enchantment.THORNS) >= thornsMax) {
            isThornsMax = true;
            thornsItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eThorns " + thornsMax,
                    isThornsMax
            );
            thornsItemStack.addUnsafeEnchantment(Enchantment.THORNS, thornsMax);
        }
        Map<Enchantment, Integer> armorEnchantments = armorItemStack.getEnchantments();
        if (armorEnchantments.containsKey(Enchantment.PROTECTION_ENVIRONMENTAL) && !(isProtectionMax)) {
            protectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eProtection " + (armorEnchantments.get(Enchantment.PROTECTION_ENVIRONMENTAL) + 1),
                    protectionPrice = getEnchantPrice(armorEnchantments.get(Enchantment.PROTECTION_ENVIRONMENTAL) + 1)
            );
            protectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, armorEnchantments.get(Enchantment.PROTECTION_ENVIRONMENTAL) + 1);
        } else if (!(isProtectionMax)) {
            protectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eProtection 1",
                    protectionPrice = getEnchantPrice(1)
            );
            protectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        }

        if (armorEnchantments.containsKey(Enchantment.PROTECTION_PROJECTILE) && !(isProjectileProtectionMax)) {
            projectileProtectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eProjectile Protection " + (armorEnchantments.get(Enchantment.PROTECTION_PROJECTILE) + 1),
                    projectionProtectionPrice = getEnchantPrice(armorEnchantments.get(Enchantment.PROTECTION_PROJECTILE) + 1)

            );
            projectileProtectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, armorEnchantments.get(Enchantment.PROTECTION_PROJECTILE) + 1);
        } else if (!(isProjectileProtectionMax)) {
            projectileProtectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eProjectile Protection 1",
                    projectionProtectionPrice = getEnchantPrice(1)
            );
            projectileProtectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
        }

        if (armorEnchantments.containsKey(Enchantment.PROTECTION_FIRE) && !(isFireProtectionMax)) {
            fireProtectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eFire Protection " + (armorEnchantments.get(Enchantment.PROTECTION_FIRE) + 1),
                    fireProtectionPrice = getEnchantPrice(armorEnchantments.get(Enchantment.PROTECTION_FIRE) + 1)

            );
            fireProtectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, armorEnchantments.get(Enchantment.PROTECTION_FIRE) + 1);
        } else if (!(isFireProtectionMax)) {
            fireProtectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eFire Protection 1",
                    fireProtectionPrice = getEnchantPrice(1)
            );
            fireProtectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1);
        }

        if (armorEnchantments.containsKey(Enchantment.PROTECTION_EXPLOSIONS) && !(isBlastProtectionMax)) {
            blastProtectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eBlast Protection " + (armorEnchantments.get(Enchantment.PROTECTION_EXPLOSIONS) + 1),
                    blastProtectionPrice = getEnchantPrice(armorEnchantments.get(Enchantment.PROTECTION_EXPLOSIONS) + 1)
            );
            blastProtectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, armorEnchantments.get(Enchantment.PROTECTION_EXPLOSIONS) + 1);
        } else if (!(isBlastProtectionMax)) {
            blastProtectionItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eBlast Protection 1",
                    blastProtectionPrice = getEnchantPrice(1)
            );
            blastProtectionItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1);
        }

        if (armorEnchantments.containsKey(Enchantment.THORNS) && !(isThornsMax)) {
            thornsItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eThorns " + (armorEnchantments.get(Enchantment.THORNS) + 1),
                    thornsPrice = getEnchantPrice(armorEnchantments.get(Enchantment.THORNS) + 1)
            );
            thornsItemStack.addUnsafeEnchantment(Enchantment.THORNS, armorEnchantments.get(Enchantment.THORNS) + 1);
        } else if (!(isThornsMax)) {
            thornsItemStack = createItemStack(
                    Material.ENCHANTED_BOOK,
                    "&eThorns 1",
                    thornsPrice = getEnchantPrice(1)
            );
            thornsItemStack.addUnsafeEnchantment(Enchantment.THORNS, 1);
        }

        /*
        Add custom items as a GUIItem and then set them to their respective slots
         */
        GUIItem armorItem = new GUIItem(armorItemStack);
        GUIItem protectionItem = new GUIItem(protectionItemStack);
        GUIItem projectileProtectionItem = new GUIItem(projectileProtectionItemStack);
        GUIItem fireProtectionItem = new GUIItem(fireProtectionItemStack);
        GUIItem blastProtectionItem = new GUIItem(blastProtectionItemStack);
        GUIItem thornsItem = new GUIItem(thornsItemStack);

        set(Column.TWO, Row.TWO, armorItem);
        set(Column.FOUR, Row.TWO, protectionItem);
        set(Column.FIVE, Row.TWO, projectileProtectionItem);
        set(Column.SIX, Row.TWO, fireProtectionItem);
        set(Column.SEVEN, Row.TWO, blastProtectionItem);
        set(Column.EIGHT, Row.TWO, thornsItem);
    }

    /**
     * Creates an item stack of the given material. Messages are taken from the specified configuration section.
     */
    private ItemStack createItemStack(Material material, String displayName, long price) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Buy: &a$") + ValueFormat.format(price, ValueFormat.THOUSANDS | ValueFormat.MILLIONS | ValueFormat.BILLIONS));
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
            if (!(isProtectionMax)) {
                if (economy.getBalance(player) >= protectionPrice) {
                    economy.withdrawPlayer(player, protectionPrice);
                    armorItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, armorItemStack.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) + 1);
                    player.sendMessage("Protection Enchant Upgraded!");
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
            if (!(isProjectileProtectionMax)) {
                if (economy.getBalance(player) >= projectionProtectionPrice) {
                    economy.withdrawPlayer(player, projectionProtectionPrice);
                    armorItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, armorItemStack.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE) + 1);
                    player.sendMessage("Projectile Protection Enchant Upgraded!");
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
            if (!(isFireProtectionMax)) {
                if (economy.getBalance(player) >= fireProtectionPrice) {
                    economy.withdrawPlayer(player, fireProtectionPrice);
                    armorItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, armorItemStack.getEnchantmentLevel(Enchantment.PROTECTION_FIRE) + 1);
                    player.sendMessage("Fire Protection Enchant Upgraded!");
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
            if (!(isBlastProtectionMax)) {
                if (economy.getBalance(player) >= blastProtectionPrice) {
                    economy.withdrawPlayer(player, blastProtectionPrice);
                    armorItemStack.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, armorItemStack.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS) + 1);
                    player.sendMessage("Blast Protection Enchant Upgraded!");
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

        // THORNS
        if (isSlot(slot, Column.EIGHT, Row.TWO)) {
            if (!(isThornsMax)) {
                if (economy.getBalance(player) >= thornsPrice) {
                    economy.withdrawPlayer(player, thornsPrice);
                    armorItemStack.addUnsafeEnchantment(Enchantment.THORNS, armorItemStack.getEnchantmentLevel(Enchantment.THORNS) + 1);
                    player.sendMessage("Thorns Enchant Upgraded!");
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

    public long getEnchantPrice(int enchantLevel) {
        return (long) ((Math.pow(2, (enchantLevel - 1))) * 500);
    }
}
