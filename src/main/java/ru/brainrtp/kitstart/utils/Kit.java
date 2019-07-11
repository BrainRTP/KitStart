package ru.brainrtp.kitstart.utils;


import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kit {

    private String name;
    private Map<String, List> equipments = new HashMap<>();
    private ItemStack itemStack;
    private String permission;

    public Kit(Map<String, List> equipments, ItemStack itemStack, String permission) {
        this.itemStack = itemStack;
        this.name = itemStack.getItemMeta().getDisplayName();
        this.equipments = equipments;
        this.permission = permission;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, List> getEquipments() {
        return equipments;
    }

    public String getPermission() {
        return permission;
    }

}