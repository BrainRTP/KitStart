package ru.brainrtp.kitstart.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import ru.brainrtp.kitstart.KitStart;
import ru.brainrtp.kitstart.gui.PlayerKitsMenu;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class Listeners implements Listener {


    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerLoginEvent event){
        if (event == null){
            return;
        }
        if (firstJoin(event.getPlayer()) || (!KitStart.usersList.isEmpty() && KitStart.usersList.contains(event.getPlayer().getName()))) {

            if (!PlayerKitsMenu.players.contains(event.getPlayer().getName()))
                PlayerKitsMenu.players.add(event.getPlayer().getName());


            Bukkit.getServer().getScheduler().runTask(KitStart.getPlugin(), () -> PlayerKitsMenu.open(event.getPlayer()));
        }

    }


    //TODO: firstJoin через API Nanit'a
    private boolean firstJoin(Player player){
        final UUID uuid = player.getUniqueId();
        final Path playerPath = Bukkit.getServer().getWorldContainer().toPath()
                .resolve(Bukkit.getWorlds().get(0).getName())
                .resolve("playerdata")
                .resolve(uuid + ".dat");
        return !Files.exists(playerPath);
    }

}
