package ru.brainrtp.kitstart;

import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.brainrtp.kitstart.commands.CommandsRegistrator;
import ru.brainrtp.kitstart.commands.ReloadCmd;
import ru.brainrtp.kitstart.gui.Menu;
import ru.brainrtp.kitstart.gui.PlayerKitsMenu;
import ru.brainrtp.kitstart.listeners.Listeners;
import ru.brainrtp.kitstart.utils.Kit;
import ru.brainrtp.kitstart.utils.XMaterial;
import ru.brainrtp.kitstart.yml.KitsConfig;
import ru.brainrtp.kitstart.yml.LanguageConfig;
import ru.brainrtp.kitstart.yml.UsersConfig;

import java.util.*;

public final class KitStart extends JavaPlugin {


    private static Plugin plugin;
    private LanguageConfig languageConfig;
    private static KitsConfig kitsConfig;
    private static UsersConfig usersConfig;
    public static List<String> usersList = new ArrayList<>();
    public static LinkedHashMap<String, Kit> kits = new LinkedHashMap<>();
    private static LuckPermsApi luckPermsAPI;

    @Override
    public void onEnable(){
        plugin = this;
        languageConfig = new LanguageConfig(this);
        kitsConfig = new KitsConfig(this);
        usersConfig = new UsersConfig(this);
        if (usersConfig.getUsers() != null)
            usersList = usersConfig.getUsers();
        CommandsRegistrator.reg(this, new ReloadCmd(languageConfig, kitsConfig), new String[] {"ksreload"}, "KitStart reload command", "/ksreload");

        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);

        if (provider != null) {
            luckPermsAPI = provider.getProvider();
        } else {
            getLogger().severe("LuckPerms not found!");
        }
        plugin.getServer().getPluginManager().registerEvents(new Listeners(),this);
        plugin.getServer().getPluginManager().registerEvents(new PlayerKitsMenu(), this);
        PlayerKitsMenu.defineStaticItems(languageConfig);
        prepareKits();
        prepareMenu(languageConfig);

    }

    public static void prepareKits(){
        kits.clear();
        kitsConfig.getFileConfiguration().getKeys(true).forEach(key -> {
            if (kitsConfig.getFileConfiguration().isConfigurationSection(key)) {
                if (key.split("[.]").length == 1) {
                    ConfigurationSection configurationSection = kitsConfig.getFileConfiguration().getConfigurationSection(key);
                    String kitName = ChatColor.translateAlternateColorCodes('&', configurationSection.getString("name"));
                    String iconItem = configurationSection.getString("iconItem");
                    List<String> description = (List<String>) configurationSection.getList("description");
                    description.replaceAll(txt -> ChatColor.translateAlternateColorCodes('&', txt));
                    ConfigurationSection equipment = configurationSection.getConfigurationSection("equipment");
                    Map<String, List> eq = new HashMap<>();
                    equipment.getKeys(true).forEach(equipments -> {
                        switch (equipments){
                            case "items":
                                eq.put("items", equipment.getList(equipments));
                                break;
                            case "helmet":
                                eq.put("helmet", Collections.singletonList(equipment.getString(equipments)));
                                break;
                            case "chestplate":
                                eq.put("chestplate", Collections.singletonList(equipment.getString(equipments)));
                                break;
                            case "leggings":
                                eq.put("leggings", Collections.singletonList(equipment.getString(equipments)));
                                break;
                            case "boots":
                                eq.put("boots", Collections.singletonList(equipment.getString(equipments)));
                                break;
                        }
                    });
                    ItemStack is = XMaterial.valueOf(iconItem).parseItem();
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(kitName);
                    im.setLore(description);
                    im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    is.setItemMeta(im);
                    String permission = configurationSection.getString("permission");
                    Kit kit = new Kit(eq, is, permission);
                    kits.put(key, kit);
                }
            }
        });
    }

    public static void prepareMenu(LanguageConfig languageConfig){
        int index = 7;
        PlayerKitsMenu.menu = new Menu(languageConfig.getMsg("menuTitle", false));

        for (Kit kit : KitStart.kits.values()) {
            PlayerKitsMenu.menu.getInventory().setItem(index+3, kit.getItemStack());
            index += 3;
        }

        ItemStack infoButton = ItemStack.deserialize(languageConfig.getFileConfiguration().getConfigurationSection("infoButton").getValues(false));
//                ItemStack infoButton = hdb.getItemHead("7822");
//        ItemMeta infoButtonMeta = infoButton.getItemMeta();
//        infoButtonMeta.setDisplayName(languageConfig.getMsg("infoButtonName",false));
//        List<String> description = (List<String>)languageConfig.getFileConfiguration().getList("infoButtonNameLore");
//        description.replaceAll(txt -> ChatColor.translateAlternateColorCodes('&', txt));
//        infoButtonMeta.setLore(description);
//        infoButtonMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        infoButton.setItemMeta(infoButtonMeta);
        ItemStack exitButton = ItemStack.deserialize(languageConfig.getFileConfiguration().getConfigurationSection("exitButton").getValues(false));
//                ItemStack exitButton = hdb.getItemHead("7827");
//        ItemMeta exitButtonMeta = exitButton.getItemMeta();
//        exitButtonMeta.setDisplayName(languageConfig.getMsg("exitButtonName", false));
//        exitButtonMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        exitButton.setItemMeta(exitButtonMeta);
//                languageConfig.getFileConfiguration().set("exitButton", exitButton.serialize());
//                languageConfig.getFileConfiguration().set("infoButton", infoButton.serialize());

//        Type.get();
        PlayerKitsMenu.menu.getInventory().setItem(22, exitButton);
        PlayerKitsMenu.menu.getInventory().setItem(4, infoButton);
//                languageConfig.save();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void debug(String message){
        Bukkit.getLogger().info(ChatColor.RED + "[DEBUG] [KitStart] > " + ChatColor.RESET + message);
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static LuckPermsApi getLuckPermsAPI(){
        return luckPermsAPI;
    }

}
