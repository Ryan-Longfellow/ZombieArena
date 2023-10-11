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

import java.util.*;

public class Join extends ChestGUI implements Clickable {
    public Join(String title, Row size) { super(title, size); }

    @Override
    public void beforeOpen(Player player) {
        int rowCount = 0;
        int columnCount = 0;

        List<String> arenaNames = ArenaHandler.getArenas();
        List<String> easyLore = new ArrayList<>();
        List<String> normalLore = new ArrayList<>();
        List<String> hardLore = new ArrayList<>();
        List<String> insaneLore = new ArrayList<>();

        /*
         TODO: Figure out why whenever doing /join, joining and arena and leaving, doing /join again duplicates the arenas

         Main assumption currently is that when a player joins the arena, it does not close the GUI properly
         Possibly forcing a close of the GUI after clicking will resolve
         */

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
                easyLore.add("&7Players: &b" + easyArena.getPlayers().size() + "&7 / &b" + easyArena.getMaxPlayers());
                easyLore.add("&7Difficulty: &aEasy");
            } else {
                easyLore.add("&7Players: &b0&7 / &b20");
                easyLore.add("&7Difficulty: &aEasy");
            }
            if (normalArena != null) {
                normalLore.add("&7Players: &b" + normalArena.getPlayers().size() + "&7 / &b" + normalArena.getMaxPlayers());
                normalLore.add("&7Difficulty: &eNormal");
            } else {
                normalLore.add("&7Players: &b0&7 / &b20");
                normalLore.add("&7Difficulty: &eNormal");
            }
            if (hardArena != null) {
                hardLore.add("&7Players: &b" + hardArena.getPlayers().size() + "&7 / &b" + hardArena.getMaxPlayers());
                hardLore.add("&7Difficulty: &6Hard");
            } else {
                hardLore.add("&7Players: &b0&7 / &b20");
                hardLore.add("&7Difficulty: &6Hard");
            }
            if (insaneArena != null) {
                insaneLore.add("&7Players: &b" + insaneArena.getPlayers().size() + "&7 / &b" + insaneArena.getMaxPlayers());
                insaneLore.add("&7Difficulty: &4Insane");
            } else {
                insaneLore.add("&7Players: &b0&7 / &b20");
                insaneLore.add("&7Difficulty: &4Insane");
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
            easyLore.clear();
            normalLore.clear();
            hardLore.clear();
            insaneLore.clear();
        }
        arenaNames.clear();
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
        beforeOpen(player);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        String itemName = Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName();
        String arenaName;
        Map<String, GameHandler> games = ZombieArena.getInstance().getGames();

        // Easy
        if (itemName.startsWith("§a")) { // Easy
            arenaName = itemName.split("§a")[1].toLowerCase();
            if (games.containsKey(arenaName + "_easy")) {
                games.get(arenaName + "_easy").addPlayer(player, arenaName);
            } else if (games.containsKey(arenaName + "_normal") || games.containsKey(arenaName + "_hard") || games.containsKey(arenaName + "_insane")) {
                player.sendMessage("This arena has already begun on a different difficulty. Please join the arena that is already started or wait for current arena to end.");
                close();
            } else {
                ZombieArena.getInstance().addGame(arenaName + "_easy", new GameHandler(GameDifficulty.EASY));
                games.get(arenaName + "_easy").addPlayer(player, arenaName);
            }
        } else if (itemName.startsWith("§e")) { // Normal
            arenaName = itemName.split("§e")[1].toLowerCase();
            if (games.containsKey(arenaName + "_normal")) {
                games.get(arenaName + "_normal").addPlayer(player, arenaName);
            } else if (games.containsKey(arenaName + "_easy") || games.containsKey(arenaName + "_hard") || games.containsKey(arenaName + "_insane")) {
                player.sendMessage("This arena has already begun on a different difficulty. Please join the arena that is already started or wait for current arena to end.");
                close();
            } else {
                ZombieArena.getInstance().addGame(arenaName + "_normal", new GameHandler(GameDifficulty.NORMAL));
                games.get(arenaName + "_normal").addPlayer(player, arenaName);
            }
        } else if (itemName.startsWith("§6")) { // Hard
            arenaName = itemName.split("§6")[1].toLowerCase();
            if (games.containsKey(arenaName + "_hard")) {
                games.get(arenaName + "_hard").addPlayer(player, arenaName);
            } else if (games.containsKey(arenaName + "_easy") || games.containsKey(arenaName + "_normal") || games.containsKey(arenaName + "_insane")) {
                player.sendMessage("This arena has already begun on a different difficulty. Please join the arena that is already started or wait for current arena to end.");
                close();
            } else {
                ZombieArena.getInstance().addGame(arenaName + "_hard", new GameHandler(GameDifficulty.HARD));
                games.get(arenaName + "_hard").addPlayer(player, arenaName);
            }
        } else if (itemName.startsWith("§4")) { // Insane
            arenaName = itemName.split("§4")[1].toLowerCase();
            if (games.containsKey(arenaName + "_insane")) {
                games.get(arenaName + "_insane").addPlayer(player, arenaName);
            } else if (games.containsKey(arenaName + "_easy") || games.containsKey(arenaName + "_normal") || games.containsKey(arenaName + "_hard")) {
                player.sendMessage("This arena has already begun on a different difficulty. Please join the arena that is already started or wait for current arena to end.");
                close();
            } else {
                ZombieArena.getInstance().addGame(arenaName + "_insane", new GameHandler(GameDifficulty.INSANE));
                games.get(arenaName + "_insane").addPlayer(player, arenaName);
            }
        }
    }
}
