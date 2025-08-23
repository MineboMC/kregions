package net.minebo.kregions.manager;

import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import lombok.Getter;
import net.minebo.cobalt.gson.Gson;
import net.minebo.kregions.KRegions;
import net.minebo.kregions.model.Flag;
import net.minebo.kregions.model.Region;
import org.bukkit.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RegionManager {
    @Getter public static List<Region> regions = new ArrayList<>();

    public static void init() {
        try {
            File mapInfo = new File(KRegions.getInstance().getDataFolder(), "regions.json");
            if (!mapInfo.exists()) {
                mapInfo.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadRegions();
    }

    public static Region getRegionByName(String name) {
        for (Region c : regions) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }

        return null;
    }

    public static Region getRegionByLocation(Location loc) {
        for (Region c : regions) {
            if(c.contains(loc)){
                return c;
            }
        }

        return null;
    }

    public static List<Region> getRegionsByFlag(Flag flag) {
        List<Region> regions = new ArrayList<>();

        for (Region c : getRegions()) {
            if(c.containsFlag(flag)) regions.add(c);
        }

        return regions;
    }

    public static void loadRegions() {
        File file = new File(KRegions.getInstance().getDataFolder(), "regions.json");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type listType = new TypeToken<List<Region>>() {}.getType();
                List<Region> loadedRegions = Gson.GSON.fromJson(reader, listType);
                if (loadedRegions != null) {
                    regions = loadedRegions; // Direct assignment prevents NPE
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveRegions() {
        File file = new File(KRegions.getInstance().getDataFolder(), "regions.json");
        try (Writer writer = new FileWriter(file)) {
            Gson.GSON.toJson(regions, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRegions() {
        File file = new File(KRegions.getInstance().getDataFolder(), "regions.json");
        if (file.exists() && file.delete()) {
            regions.clear();
        }
    }

    public static void jsontoRegion(BasicDBObject o) {
        String json = o.toJson(); // Convert BasicDBObject to JSON string
        Region region = Region.fromJson(BasicDBObject.parse(json));
        regions.add(region);
    }
}
