package net.minebo.kregions.flag;

import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Flag;
import net.minebo.kregions.model.Region;
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

        Region rg = RegionManager.getRegionByLocation(event.getEntity().getLocation());
        if (rg == null) return;

        // Check if the flag applies in this region
        if (rg.containsFlag(this)) {
            event.setCancelled(true); // Cancel incoming damage
        }
    }

    @EventHandler
    public void onPlayerDealDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();

        Region damagerRg = RegionManager.getRegionByLocation(damager.getLocation());
        Region victimRg = RegionManager.getRegionByLocation(event.getEntity().getLocation());
        if (damagerRg == null || victimRg == null) return;

        // Check if the flag applies where the attacker is
        if (damagerRg.containsFlag(this) || victimRg.containsFlag(this)) {
            event.setCancelled(true); // Cancel outgoing damage
        }
    }

}
