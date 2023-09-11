package me.Visionexe.ZombieArena.GUI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Interface for a clickable GUI.
 */
public interface Clickable extends Listener {
	
	@EventHandler
	default void clickEvent(InventoryClickEvent event) {
		if (!(this instanceof GUI))
			return;
		
		GUI gui = (GUI)this;
		
		if (gui.inventory == null)
			return;
		
		if (!gui.inventory.equals(event.getInventory()))
			return;
			
		onClick(event);
	}
	
	void onClick(InventoryClickEvent event);
}
