package xyz.marshalldev.mobrewards;

import xyz.marshalldev.mobrewards.handler.ConfigHandler;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.marshalldev.mobrewards.handler.MobHandler;

public final class MobRewards extends JavaPlugin {

    private ConfigHandler configHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        configHandler = new ConfigHandler(this, "config.yml");
        MobHandler mobHandler = new MobHandler();
        mobHandler.populateMobs(configHandler);
        getServer().getPluginManager().registerEvents(mobHandler, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
