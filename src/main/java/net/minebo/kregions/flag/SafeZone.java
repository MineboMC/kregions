package net.minebo.kregions.flag;

import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Flag;
import net.minebo.kregions.model.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SafeZone extends Flag {
    private final Set<UUID> fellFromSafezone = ConcurrentHashMap.newKeySet();

    public SafeZone() {
        super("SafeZone");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        UUID id = p.getUniqueId();

        // If player is currently falling and we haven't already marked them, check their origin region.
        if (p.getFallDistance() > 0f && !fellFromSafezone.contains(id)) {
            Region originRegion = RegionManager.getRegionByLocation(event.getFrom());
            if (originRegion != null && originRegion.containsFlag(this)) {
                fellFromSafezone.add(id);
            }
        }

        // If player has landed (fallDistance reset), clear the mark.
        if (p.getFallDistance() == 0f && fellFromSafezone.contains(id)) {
            fellFromSafezone.remove(id);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        UUID id = p.getUniqueId();

        // If it's fall damage and the player started the fall in a SafeZone, cancel it and clear the mark.
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && fellFromSafezone.contains(id)) {
            event.setCancelled(true);
            fellFromSafezone.remove(id);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        fellFromSafezone.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        fellFromSafezone.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        fellFromSafezone.remove(event.getPlayer().getUniqueId());
    }
}