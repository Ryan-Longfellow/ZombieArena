package me.Visionexe.ZombieArena.GUI;

import org.bukkit.inventory.ItemStack;

/**
 * Represents a GUI Item.
 */
public class GUIItem {
	
	private ItemStack itemStack;
	
	private GUIAction action;
	
	public GUIItem(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
	public GUIItem(ItemStack itemStack, GUIAction action) {
		this(itemStack);
		
		this.action = action;
	}
	
	public void setAction(GUIAction action) {
		this.action = action;
	}
	
	public void onClick() {
		action.execute();
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
}
