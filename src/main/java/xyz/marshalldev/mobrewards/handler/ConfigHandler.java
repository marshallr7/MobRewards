package xyz.marshalldev.mobrewards.handler;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.marshalldev.mobrewards.entity.Mob;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {

    private final JavaPlugin plugin;
    private FileConfiguration config = null;
    private File configFile = null;
    private final String configName;

    public ConfigHandler(JavaPlugin plugin, String configName) {
        this.plugin = plugin;
        this.configName = configName;
        createConfig();
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadCustomConfig();
        }
        return config;
    }

    public void reloadCustomConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), configName);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defConfigStream = plugin.getResource(configName);
        if (defConfigStream != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream)));
        }
    }

    public void saveCustomConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), configName);
        }
        if (!configFile.exists()) {
            plugin.saveResource(configName, false);
        }
    }

    private void createConfig() {
        saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public List<Mob> loadConfig() {
        List<Mob> mobs = new ArrayList<>();
        for (String key : config.getKeys(false)) {
            List<String> commands = config.getStringList(key + ".command");
            List<String> mobNames = config.getStringList(key + ".mob");
            List<String> worlds = config.getStringList(key + ".world");
            List<String> regions = config.getStringList(key + ".region");
            double chance = config.getDouble(key + ".chance");
            String message = config.getString(key + ".message");

            Mob mob = new Mob(commands, mobNames, worlds, regions, chance, message);
            mobs.add(mob);
        }
        return mobs;
    }

}
