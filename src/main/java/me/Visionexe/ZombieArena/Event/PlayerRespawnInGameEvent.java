package me.Visionexe.ZombieArena.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRespawnInGameEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private PlayerRespawnCause cause;
    private boolean cancel;

    public PlayerRespawnInGameEvent(Player player, PlayerRespawnCause cause)
    {
        this.player = player;
        this.cause = cause;
    }

    public Player getPlayer()
    {
        return player;
    }

    public PlayerRespawnCause getCause()
    {
        return cause;
    }

    public boolean isCancelled()
    {
        return cancel;
    }

    public void setCancelled(boolean cancel)
    {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
