package me.Visionexe.ZombieArena.Listener;

import fr.mrmicky.fastboard.FastBoard;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;

import me.Visionexe.ZombieArena.Game.GameHandler;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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
        if (gameHandler.getPlayers().contains(player) && event.getFinalDamage() >= player.getHealth()) {
            event.setCancelled(true);
            gameHandler.getPlayerStats(player).setAlive(false);
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(gameHandler.getArenaHandler().getPlayerSpawn(gameHandler.getPlayerStats(player).getArenaName()));
        }

    }
}
