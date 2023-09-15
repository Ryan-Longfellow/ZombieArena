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
        PlayerWrapper.get(event.getPlayer());

        FastBoard board = new FastBoard(event.getPlayer());
        ZombieArena.getInstance().boards.put(event.getPlayer().getUniqueId(), board);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerWrapper.remove(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        GameHandler gameHandler = ZombieArena.getInstance().getGameHandler();
        Player player = ((Player) event.getEntity()).getPlayer();
        // Verify player exists, is in game and is going to die
        if (player != null && gameHandler.getPlayers().contains(player) && event.getFinalDamage() >= player.getHealth()) {
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
