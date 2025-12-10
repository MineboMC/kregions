package net.minebo.kregions.flag;

import net.minebo.kregions.KRegions;
import net.minebo.kregions.manager.FlagManager;
import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Flag;
import net.minebo.kregions.model.Region;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
        if(shouldCancel(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Region rg = RegionManager.getRegionByLocation(event.getBlockPlaced().getLocation());

        if (rg == null) return;

        if(Bukkit.getPluginManager().isPluginEnabled("Brawl")) {
            if(event.getBlockPlaced().getType() == Material.COBWEB && !rg.containsFlag(FlagManager.getFlagByName("SafeZone"))) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        event.getBlockPlaced().setType(Material.AIR);
                    }
                }.runTaskLater(KRegions.getInstance(), 5*20);

                return;
            }
        }

        if(shouldCancel(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        // Use getBlockClicked().getLocation() because that's where the fluid is placed
        if(shouldCancel(event.getPlayer(), event.getBlockClicked().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if(shouldCancel(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}