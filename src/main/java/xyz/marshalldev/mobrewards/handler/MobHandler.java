package xyz.marshalldev.mobrewards.handler;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import xyz.marshalldev.mobrewards.entity.Mob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MobHandler implements Listener {
    // This Map holds other Maps. The outer Map's key is the world name, and its value is another Map.
    // The inner Map's key is the mob name, and its value is the Mob object.
    private final Map<String, Map<String, Mob>> mobsByWorld = new HashMap<>();
    private final Random random = new Random();


    public void populateMobs(ConfigHandler configHandler) {
        List<Mob> loadedMobs = configHandler.loadConfig();

        for (Mob mob : loadedMobs) {
            for (String world : mob.getWorlds()) {
                mobsByWorld.computeIfAbsent(world, k -> new HashMap<>());

                for (String mobName : mob.getMobNames()) {
                    mobsByWorld.get(world).put(mobName, mob);
                }
            }
        }
    }

    public Map<String, Map<String, Mob>> getMobsByWorld() {
        return mobsByWorld;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        if (killer == null) {
            return;
        }

        String worldName = entity.getWorld().getName();
        String mobName = entity.getType().toString().toLowerCase();

        if (mobsByWorld.containsKey(worldName) && mobsByWorld.get(worldName).containsKey(mobName)) {
            Mob mob = mobsByWorld.get(worldName).get(mobName);

            if (isPlayerInCorrectRegion(killer, mob)) {
                double chance = mob.getChance();

                if (random.nextDouble() * 100 <= chance) {
                    executeMobAction(killer, mob);
                }
            }
        }
    }

    private boolean isPlayerInCorrectRegion(Player player, Mob mob) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        Location location = player.getLocation();
        com.sk89q.worldedit.util.Location weLocation = BukkitAdapter.adapt(location);

        for (String regionName : mob.getRegions()) {
            if (query.testState(weLocation, WorldGuardPlugin.inst().wrapPlayer(player), Flags.BUILD)) {
                return true;
            }
        }
        return false;
    }

    private void executeMobAction(Player player, Mob mob) {
        for (String command : mob.getCommands()) {
            String processedCommand = command.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), processedCommand);
        }
    }
}
