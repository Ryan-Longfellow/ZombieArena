package me.Visionexe.ZombieArena.GUI.Game;

import me.Visionexe.ZombieArena.GUI.ChestGUI;
import me.Visionexe.ZombieArena.GUI.Clickable;
import me.Visionexe.ZombieArena.GUI.GUIItem;
import me.Visionexe.ZombieArena.GUI.Row;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class JoinGUI extends ChestGUI implements Clickable {
    public JoinGUI(String title, Row size) { super(title, size); }

    @Override
    public void beforeOpen(Player player) {
        /*
        Green Wool for Easy
        Yellow Wool for Normal
        Orange Wool for Hard
        Red Wool for Insane
         */
        List<String> easyLore = new ArrayList<>();
        List<String> normalLore = new ArrayList<>();
        List<String> hardLore = new ArrayList<>();
        List<String> insaneLore = new ArrayList<>();

        // Will be used in the future to get each aren
//        for (String arena : ZombieArena.getInstance().getArenasFile().getKeys(false)) {
//
//        }

        ItemStack easyItemStack = createItemStack(
                Material.GREEN_WOOL,
                "&aNDU",
                easyLore
        );
        ItemStack normalItemStack = createItemStack(
                Material.YELLOW_WOOL,
                "&eNDU",
                normalLore
        );
        ItemStack hardItemStack = createItemStack(
                Material.ORANGE_WOOL,
                "&6NDU",
                hardLore
        );
        ItemStack insaneItemStack = createItemStack(
                Material.RED_WOOL,
                "&4NDU",
                insaneLore
        );

        GUIItem easyItem = new GUIItem(easyItemStack);
        GUIItem normalItem = new GUIItem(normalItemStack);
        GUIItem hardItem = new GUIItem(hardItemStack);
        GUIItem insaneItem = new GUIItem(insaneItemStack);
    }

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

    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }
}
