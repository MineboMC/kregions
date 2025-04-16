package net.minebo.kregions.model;

import net.minebo.kregions.KRegions;
import net.minebo.kregions.manager.FlagManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class Flag implements Listener {
    public String name;

    public Flag(String name) {
        this.name = name;

        FlagManager.flags.add(this);
        Bukkit.getPluginManager().registerEvents(this, KRegions.getInstance());
    }
}
