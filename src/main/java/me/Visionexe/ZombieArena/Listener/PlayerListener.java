package me.Visionexe.ZombieArena.Listener;

import fr.mrmicky.fastboard.FastBoard;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;

import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Get PlayerWrapper to either initialize if player is new or get existing data
        PlayerWrapper.get(event.getPlayer());
        // Force a load after PlayerWrapper is obtained; this is required as the scoreboard will break without it if a new player joins
        PlayerWrapper.get(event.getPlayer()).load();

        FastBoard board = new FastBoard(event.getPlayer());
        ZombieArena.getInstance().boards.put(event.getPlayer().getUniqueId(), board);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerWrapper.remove(event.getPlayer());

        FastBoard board = new FastBoard(event.getPlayer());
        ZombieArena.getInstance().boards.put(event.getPlayer().getUniqueId(), board);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = ((Player) event.getEntity()).getPlayer();
        GameHandler gameHandler = ZombieArena.getInstance().getGamePlayerIn(player);
        // Verify player exists, is in game and is going to die
        if (player != null && ZombieArena.getInstance().getPlayersInGame().contains(player) && event.getFinalDamage() >= player.getHealth()) {
            // Cancel the damage
            event.setCancelled(true);
            // Set the player to not be alive
            gameHandler.getPlayerStats(player).setAlive(false);
            gameHandler.getPlayerStats(player).registerDeath();
            // Heal player so they should be at full health
            gameHandler.healPlayer(player);
            // Put them in spectator
            player.setGameMode(GameMode.SPECTATOR);
            // Teleport them to arena spawn
            player.teleport(gameHandler.getArenaHandler().getPlayerSpawn(gameHandler.getPlayerStats(player).getArenaName()));
        }
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getEntity();
        player.setFoodLevel(20);
    }
}
