package ru.brainrtp.kitstart.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import ru.brainrtp.kitstart.KitStart;
import ru.brainrtp.kitstart.yml.KitsConfig;
import ru.brainrtp.kitstart.yml.LanguageConfig;

public class ReloadCmd  implements CommandExecutor {
    private LanguageConfig lang;
    private KitsConfig kitsConfig;

    public ReloadCmd(LanguageConfig lang, KitsConfig kitsConfig){
        this.lang = lang;
        this.kitsConfig = kitsConfig;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("ksreload")) {
            return false;
        }
        if (!sender.hasPermission("bwk.reload")) {
            sender.sendMessage(lang.getMsg("permissionDeny", true));
            return true;
        }
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
        lang.reload();
        kitsConfig.reload();
        KitStart.prepareKits();
        KitStart.prepareMenu(lang);
        sender.sendMessage(lang.getMsg("reloadSuccess", true));
        return true;
    }
}