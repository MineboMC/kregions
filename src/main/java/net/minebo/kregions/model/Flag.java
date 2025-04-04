package net.minebo.kregions.model;

import net.minebo.kregions.manager.FlagManager;

public class Flag {
    public String name;

    public Flag(String name) {
        this.name = name;

        FlagManager.flags.add(this);
    }
}
