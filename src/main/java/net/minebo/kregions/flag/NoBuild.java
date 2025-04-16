package net.minebo.kregions.flag;

import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Flag;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class NoBuild extends Flag {
    public NoBuild() {
        super("NoBuild");
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
}
