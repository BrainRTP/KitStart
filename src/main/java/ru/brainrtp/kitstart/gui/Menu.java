package ru.brainrtp.kitstart.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.brainrtp.kitstart.utils.ColorUtils;

public class Menu implements InventoryHolder {

	private Inventory inv;
	  
	public Menu(String title){
		this.inv = Bukkit.createInventory(this, 27, ColorUtils.color(title));
	}
	  
	public Inventory getInventory(){
		return this.inv;
	}
}
