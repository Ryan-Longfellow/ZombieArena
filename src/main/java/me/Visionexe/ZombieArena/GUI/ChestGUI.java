package me.Visionexe.ZombieArena.GUI;

import me.Visionexe.ZombieArena.Log;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

/**
 * Abstract class for a chest-like type of GUI.
 */
public abstract class ChestGUI extends GUI {

	private Row size;

	/**
	 * Constructs an instance of a Chest GUI.
	 */
	public ChestGUI(String title, Row size) {
		super(title);

		this.size = size;
	}

	/**
	 * Sets the specified GUI Item to every slot of the GUI.
	 */
	public void set(GUIItem item) {
		for (Row row : Row.values()) {
			if (row.get() > size.get())
				break;

			for (Column column : Column.values()) {
				set(column, row, item);
			}
		}
	}

	/**
	 * Sets the specified GUI Item to the given row of the GUI.
	 */
	public void set(Row row, GUIItem item) {
		if (row.get() > size.get())
			return;

		for (Column column : Column.values())
			set(column, row, item);
	}

	/**
	 * Sets the specified GUI Item to the given column of the GUI.
	 */
	public void set(Column column, GUIItem item) {
		for (Row row : Row.values()) {
			if (row.get() > size.get())
				break;

			set(column, row, item);
		}
	}

	/**
	 * Sets the specified GUI Item to the given row and column of the GUI.
	 */
	public void set(Column column, Row row, GUIItem item) {
		content.put(Slot.of(column, row), item);
	}

	/**
	 * Checks whether the specified slot corresponds to the slot at the given row and column.
	 */
	public boolean isSlot(int slot, Column column, Row row) {
		Log.debug("Checking if slot '" + slot + "' is at column '" + column.name() + "' and row '" + row.name() + "'.");
		return slot == row.get() * 9 + column.get();
	}

	/**
	 * Returns the inventory of the GUI.
	 */
	@Override
	Inventory getInventory() {
		this.inventory = Bukkit.createInventory(null, 9 + size.get() * 9, getTitle());
		return this.inventory;
	}
}
