package me.Visionexe.ZombieArena.Listener;

import fr.mrmicky.fastboard.FastBoard;
import me.Visionexe.ZombieArena.Entity.PlayerWrapper;

import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
}
