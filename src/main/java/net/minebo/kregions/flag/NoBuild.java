package net.minebo.kregions.flag;

import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Flag;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class NoBuild extends Flag implements Listener {
    public NoBuild() {
        super("NoBuild");
    }

    private boolean shouldCancel(Player player, Location location) {
        return RegionManager.getRegionByLocation(location) != null
                && RegionManager.getRegionByLocation(location).containsFlag(this)
                && (player.getGameMode() != GameMode.CREATIVE
                || !player.isOp()
                || player.hasMetadata("modmode"));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (shouldCancel(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (shouldCancel(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        // Use getBlockClicked().getLocation() because that's where the fluid is placed
        if (shouldCancel(event.getPlayer(), event.getBlockClicked().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (shouldCancel(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}