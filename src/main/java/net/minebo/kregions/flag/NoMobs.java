package net.minebo.kregions.flag;

import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Flag;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Arrays;

public class NoMobs extends Flag {
    public NoMobs() {
        super("NoMobs");
    }

    public EntityType[] allowedEntities = {EntityType.ENDER_PEARL, EntityType.SPLASH_POTION, EntityType.DROPPED_ITEM, EntityType.EXPERIENCE_ORB, EntityType.THROWN_EXP_BOTTLE};

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if(RegionManager.getRegionByLocation(event.getLocation()) != null) {
            if (RegionManager.getRegionByLocation(event.getLocation()).containsFlag(this)) {
                if(Arrays.asList(allowedEntities).contains(event.getEntityType())) {
                    return;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMobBreak(EntityChangeBlockEvent event) {
        if(RegionManager.getRegionByLocation(event.getBlock().getLocation()) != null) {
            if (RegionManager.getRegionByLocation(event.getBlock().getLocation()).containsFlag(this)) {
                event.setCancelled(true);
            }
        }
    }
}
