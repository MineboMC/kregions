package net.minebo.kregions.flag;

import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Flag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class NoMobs extends Flag {
    public NoMobs() {
        super("NoMobs");
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if(RegionManager.getRegionByLocation(event.getLocation()) != null) {
            if (RegionManager.getRegionByLocation(event.getLocation()).containsFlag(this)) {
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
