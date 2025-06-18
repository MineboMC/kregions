package net.minebo.kregions.flag;

import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Flag;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoDamage extends Flag {
    public NoDamage() {
        super("NoDamage");
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        // Check if the flag applies in this region
        if (RegionManager.getRegionByLocation(player.getLocation()).containsFlag(this)) {
            event.setCancelled(true); // Cancel incoming damage
        }
    }

    @EventHandler
    public void onPlayerDealDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();

        // Check if the flag applies where the attacker is
        if (RegionManager.getRegionByLocation(damager.getLocation()).containsFlag(this) || RegionManager.getRegionByLocation(event.getEntity().getLocation()).containsFlag(this)) {
            event.setCancelled(true); // Cancel outgoing damage
        }
    }

}
