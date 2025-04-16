package net.minebo.kregions.flag;

import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Flag;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SafeZone extends Flag {
    public SafeZone() {
        super("SafeZone");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(RegionManager.getRegionByLocation(event.getBlock().getLocation()) != null) {
            if (RegionManager.getRegionByLocation(event.getBlock().getLocation()).containsFlag(this)) {
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent event) {
        if(RegionManager.getRegionByLocation(event.getBlock().getLocation()) != null) {
            if (RegionManager.getRegionByLocation(event.getBlock().getLocation()).containsFlag(this)) {
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            }
        }
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
