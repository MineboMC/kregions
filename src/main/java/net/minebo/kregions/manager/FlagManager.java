package net.minebo.kregions.manager;

import net.minebo.kregions.flag.NoBuild;
import net.minebo.kregions.flag.NoMobs;
import net.minebo.kregions.flag.SafeZone;
import net.minebo.kregions.model.Flag;

import java.util.ArrayList;

public class FlagManager {
    public static ArrayList<Flag> flags;

    public static void init() {
        flags = new ArrayList<>();

        new SafeZone();
        new NoMobs();
        new NoBuild();
    }

    public static Flag getFlagByName(String name) {
        for (Flag flag : flags) {
            if (flag.name.equalsIgnoreCase(name)) { // Use .equalsIgnoreCase() for string comparison
                return flag;
            }
        }

        return null;
    }
}
