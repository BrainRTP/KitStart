package ru.brainrtp.kitstart.yml;

import org.bukkit.plugin.java.JavaPlugin;
import ru.brainrtp.kitstart.KitStart;
import ru.brainrtp.kitstart.utils.ColorUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UsersConfig extends YmlConfig {

    private List<String> usersList = new ArrayList<>();

    public UsersConfig(JavaPlugin plugin) {
        super(plugin, "users", true);
        this.update();
    }

    @Override
    public void reload() {
        super.reload();
        this.update();
    }

    private void update() {
        usersList = (yml.isList("users") ? (List<String>) yml.getList("users") : null);
    }

    public List<String> getUsers() {
        return usersList;
    }

}