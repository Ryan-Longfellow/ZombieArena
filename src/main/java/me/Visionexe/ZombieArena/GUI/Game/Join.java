package me.Visionexe.ZombieArena.GUI.Game;

import me.Visionexe.ZombieArena.GUI.*;
import me.Visionexe.ZombieArena.Game.ArenaHandler;
import me.Visionexe.ZombieArena.Game.GameDifficulty;
import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Join extends ChestGUI implements Clickable {
    public Join(String title, Row size) { super(title, size); }

    @Override
    public void beforeOpen(Player player) {
        // Check into clearing inventory of all items on open
        /*
        Green Wool for Easy
        Yellow Wool for Normal
        Orange Wool for Hard
        Red Wool for Insane
         */
        int rowCount = 0;
        int columnCount = 0;

        List<String> arenaNames = ArenaHandler.getArenas();
        List<String> easyLore = new ArrayList<>();
        List<String> normalLore = new ArrayList<>();
        List<String> hardLore = new ArrayList<>();
        List<String> insaneLore = new ArrayList<>();

        /*
        Loop through each arena
        For each arena:
        - Create an ItemStack for each difficulty
        - Pass into GUIItem
        - Set GUIItem to next available slot
         */
        for (String arena : arenaNames) {
            /*
            Get GameHandlers for all difficulties
            If GameHandler exists, get the players in the arena
             */
            GameHandler easyArena = ZombieArena.getInstance().getGames().get(arena + "_easy");
            GameHandler normalArena = ZombieArena.getInstance().getGames().get(arena + "_normal");
            GameHandler hardArena = ZombieArena.getInstance().getGames().get(arena + "_hard");
            GameHandler insaneArena = ZombieArena.getInstance().getGames().get(arena + "_insane");
            if (easyArena != null) {
                easyLore.add("Players: " + easyArena.getPlayers().size() + " / " + easyArena.getMaxPlayers());
                easyLore.add("Difficulty: Easy");
            } else {
                easyLore.add("Players: 0 / 10");
                easyLore.add("Difficulty: Easy");
            }
            if (normalArena != null) {
                normalLore.add("Players: " + normalArena.getPlayers().size() + " / " + normalArena.getMaxPlayers());
                normalLore.add("Difficulty: Normal");
            } else {
                normalLore.add("Players: 0 / 10");
                normalLore.add("Difficulty: Normal");
            }
            if (hardArena != null) {
                hardLore.add("Players: " + hardArena.getPlayers().size() + " / " + hardArena.getMaxPlayers());
                hardLore.add("Difficulty: Hard");
            } else {
                hardLore.add("Players: 0 / 10");
                hardLore.add("Difficulty: Hard");
            }
            if (insaneArena != null) {
                insaneLore.add("Players: " + insaneArena.getPlayers().size() + " / " + insaneArena.getMaxPlayers());
                insaneLore.add("Difficulty: Insane");
            } else {
                insaneLore.add("Players: 0 / 10");
                insaneLore.add("Difficulty: Insane");
            }

            // Easy Item
            set(Column.of(columnCount++), Row.of(rowCount), new GUIItem (createItemStack(
                    Material.GREEN_WOOL,
                    "&a" + arena.toUpperCase(),
                    easyLore
            )));
            // Normal Item
            set(Column.of(columnCount++), Row.of(rowCount), new GUIItem (createItemStack(
                    Material.YELLOW_WOOL,
                    "&e" + arena.toUpperCase(),
                    normalLore
            )));
            // Hard Item
            set(Column.of(columnCount++), Row.of(rowCount), new GUIItem (createItemStack(
                    Material.ORANGE_WOOL,
                    "&6" + arena.toUpperCase(),
                    hardLore
            )));
            // Insane Item
            set(Column.of(columnCount), Row.of(rowCount), new GUIItem (createItemStack(
                    Material.RED_WOOL,
                    "&4" + arena.toUpperCase(),
                    insaneLore
            )));
            rowCount++;
            columnCount = 0;
        }
        rowCount = 0;
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
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        String itemName = Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName();
        String arenaName;

        // Easy
        if (itemName.startsWith("§a")) { // Easy
            arenaName = itemName.split("§a")[1].toLowerCase();
            if (ZombieArena.getInstance().getGames().containsKey(arenaName + "_easy")) {
                ZombieArena.getInstance().getGames().get(arenaName + "_easy").addPlayer(player, arenaName);
            } else {
                ZombieArena.getInstance().addGame(arenaName + "_easy", new GameHandler(GameDifficulty.EASY));
                ZombieArena.getInstance().getGames().get(arenaName + "_easy").addPlayer(player, arenaName);
            }
        } else if (itemName.startsWith("§e")) { // Normal
            arenaName = itemName.split("§e")[1].toLowerCase();
            if (ZombieArena.getInstance().getGames().containsKey(arenaName + "_normal")) {
                ZombieArena.getInstance().getGames().get(arenaName + "_normal").addPlayer(player, arenaName);
            } else {
                ZombieArena.getInstance().addGame(arenaName + "_normal", new GameHandler(GameDifficulty.NORMAL));
                ZombieArena.getInstance().getGames().get(arenaName + "_normal").addPlayer(player, arenaName);
            }
        } else if (itemName.startsWith("§6")) { // Hard
            arenaName = itemName.split("§6")[1].toLowerCase();
            if (ZombieArena.getInstance().getGames().containsKey(arenaName + "_hard")) {
                ZombieArena.getInstance().getGames().get(arenaName + "_hard").addPlayer(player, arenaName);
            } else {
                ZombieArena.getInstance().addGame(arenaName + "_hard", new GameHandler(GameDifficulty.HARD));
                ZombieArena.getInstance().getGames().get(arenaName + "_hard").addPlayer(player, arenaName);
            }
        } else if (itemName.startsWith("§4")) { // Insane
            arenaName = itemName.split("§4")[1].toLowerCase();
            if (ZombieArena.getInstance().getGames().containsKey(arenaName + "_insane")) {
                ZombieArena.getInstance().getGames().get(arenaName + "_insane").addPlayer(player, arenaName);
            } else {
                ZombieArena.getInstance().addGame(arenaName + "_insane", new GameHandler(GameDifficulty.INSANE));
                ZombieArena.getInstance().getGames().get(arenaName + "_insane").addPlayer(player, arenaName);
            }
        }
    }
}
