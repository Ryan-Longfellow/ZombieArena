package me.Visionexe.ZombieArena.GUI;

import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic abstract GUI class.
 */
public abstract class GUI {

	private String title;

	protected HashMap<Slot, GUIItem> content = new HashMap<>();

	protected Inventory inventory;

	/**
	 * Constructs an instance of the GUI class.
	 */
	public GUI(String title) {
		this.title = title;

		registerEvents();
	}

	/**
	 * Registers all events related to the GUI.
	 */
	private void registerEvents() {
		Log.debug("Registering GUI event(s)...");
		if (this instanceof Clickable) {
			Clickable clickable = (Clickable) this;
			Bukkit.getPluginManager().registerEvents(clickable, ZombieArena.getInstance());
		}
		Log.debug("GUI event(s) registered!");
	}

	/**
	 * Returns the title of the GUI.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the GUI. Required the GUI to be re-opened for changes to take effect.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Opens the GUI to the specified Player.
	 */
	public void open(Player player) {
		this.inventory = getInventory();
		beforeOpen(player);
		player.openInventory(inventory);
		updateContent();
		onOpen(player);
	}

	/**
	 * Forces an update of the GUI content.
	 */
	public void updateContent() {
		for (Map.Entry<Slot, GUIItem> entry : content.entrySet()) {
			Slot slot = entry.getKey();
			GUIItem item = entry.getValue();

			this.inventory.setItem(slot.getRow().get() * 9 + slot.getColumn().get(), item.getItemStack());
		}
	}

	/**
	 * Close the GUI to all it's viewers.
	 */
	public void close() {
		for (HumanEntity humanEntity : this.inventory.getViewers()) {
			humanEntity.closeInventory();
		}
	}
	
	/*public void set(GUIItem item) {
		content.put(new Slot, item);
	}*/

	/**
	 * Called before the GUI is opened. Set the GUI appearance here.
	 */
	public abstract void beforeOpen(Player player);

	/**
	 * Called when the GUI was opened.
	 */
	public abstract void onOpen(Player player);

	/**
	 * Returns the Inventory of the GUI.
	 */
	abstract Inventory getInventory();

}
