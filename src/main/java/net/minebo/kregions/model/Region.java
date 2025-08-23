package net.minebo.kregions.model;

import com.mongodb.BasicDBObject;
import net.minebo.kregions.KRegions;
import net.minebo.kregions.manager.FlagManager;
import net.minebo.kregions.manager.RegionManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minebo.cobalt.gson.Gson;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

@AllArgsConstructor
public class Region implements Iterable<Coordinate> {

    @Getter @Setter private String world;
    @Getter @Setter private int x1;
    @Getter @Setter private int y1;
    @Getter @Setter private int z1;
    @Getter @Setter private int x2;
    @Getter @Setter private int y2;
    @Getter @Setter private int z2;
    @Getter @Setter private List<Flag> flags;
    @Getter @Setter private String name;

    public static Region fromJson(final BasicDBObject obj) {
        Location loc1 = Gson.GSON.fromJson(obj.get("Location1").toString(), Location.class);
        Location loc2 = Gson.GSON.fromJson(obj.get("Location2").toString(), Location.class);
        String name = obj.getString("Name");

        Region region = new Region(name, loc1, loc2);
        region.flags = new ArrayList<>();

        List<String> flagNames = (List<String>) obj.get("flags");
        if (flagNames != null) {
            for (String flagName : flagNames) {
                region.flags.add(FlagManager.getFlagByName(flagName));
            }
        }

        RegionManager.getRegions().add(region);
        return region;
    }

    public Region(final String name, final Location corner1, final Location corner2) {
        this(corner1.getWorld().getName(), corner1.getBlockX(), corner1.getBlockY(), corner1.getBlockZ(),
                corner2.getBlockX(), corner2.getBlockY(), corner2.getBlockZ(), new ArrayList<>(), name);
        RegionManager.getRegions().add(this);
        RegionManager.saveRegions();
    }

    public Region(final Region copyFrom) {
        this(copyFrom.world, copyFrom.x1, copyFrom.y1, copyFrom.z1,
                copyFrom.x2, copyFrom.y2, copyFrom.z2,
                new ArrayList<>(copyFrom.flags), copyFrom.name);
        RegionManager.getRegions().add(this);
        RegionManager.saveRegions();
    }

    public BasicDBObject json() {
        final BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("Name", this.name);

        final World world = KRegions.getInstance().getServer().getWorld(this.getWorld());

        dbObject.put("Location1", BasicDBObject.parse(Gson.GSON.toJson(new Location(world, this.x1, this.y1, this.z1))));
        dbObject.put("Location2", BasicDBObject.parse(Gson.GSON.toJson(new Location(world, this.x2, this.y2, this.z2))));

        // Ensure flag names are properly serialized
        List<String> flagNames = new ArrayList<>();
        for (Flag flag : this.flags) {
            if (flag != null && flag.name != null) {
                flagNames.add(flag.name);
            }
        }

        dbObject.put("flags", flagNames);
        return dbObject;
    }


    public boolean containsFlag(Flag flag) {
        return flags.stream().anyMatch(f -> f.name.equalsIgnoreCase(flag.name));
    }

    public void addFlag(Flag flag) {
        if (!containsFlag(flag)) {
            flags.add(flag);
        }
    }

    public void removeFlag(Flag flag) {
        flags.removeIf(f -> f.name.equalsIgnoreCase(flag.name));
    }

    public Location getMinimumPoint() {
        return new Location(KRegions.getInstance().getServer().getWorld(this.world),
                Math.min(this.x1, this.x2),
                Math.min(this.y1, this.y2),
                Math.min(this.z1, this.z2));
    }

    public Location getMaximumPoint() {
        return new Location(KRegions.getInstance().getServer().getWorld(this.world),
                Math.max(this.x1, this.x2),
                Math.max(this.y1, this.y2),
                Math.max(this.z1, this.z2));
    }

    public boolean contains(final int x, final int y, final int z, final String world) {
        return world != null && world.equalsIgnoreCase(this.world) &&
                y >= Math.min(y1, y2) && y <= Math.max(y1, y2) &&
                contains(x, z, world);
    }

    public boolean contains(final int x, final int z, final String world) {
        return world != null && world.equalsIgnoreCase(this.world) &&
                x >= Math.min(x1, x2) && x <= Math.max(x1, x2) &&
                z >= Math.min(z1, z2) && z <= Math.max(z1, z2);
    }

    public boolean contains(final Location location) {
        return this.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());
    }

    public boolean contains(final Block block) {
        return this.contains(block.getLocation());
    }

    public boolean contains(final Player player) {
        return this.contains(player.getLocation());
    }

    public Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        for (Player player : KRegions.getInstance().getServer().getOnlinePlayers()) {
            if (this.contains(player)) {
                players.add(player);
            }
        }
        return players;
    }

    public String getFriendlyName() {
        return "(" + this.world + ", " + this.x1 + ", " + this.y1 + ", " + this.z1 + ") - (" +
                this.world + ", " + this.x2 + ", " + this.y2 + ", " + this.z2 + ")";
    }

    public Region expand(final CuboidDirection dir, final int amount) {
        switch (dir) {
            case North: return new Region(this.world, this.x1 - amount, this.y1, this.z1, this.x2, this.y2, this.z2, this.flags, this.name);
            case South: return new Region(this.world, this.x1, this.y1, this.z1, this.x2 + amount, this.y2, this.z2, this.flags, this.name);
            case East:  return new Region(this.world, this.x1, this.y1, this.z1 - amount, this.x2, this.y2, this.z2, this.flags, this.name);
            case West:  return new Region(this.world, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + amount, this.flags, this.name);
            case Down:  return new Region(this.world, this.x1, this.y1 - amount, this.z1, this.x2, this.y2, this.z2, this.flags, this.name);
            case Up:    return new Region(this.world, this.x1, this.y1, this.z1, this.x2, this.y2 + amount, this.z2, this.flags, this.name);
            default:    throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    public Region outset(final CuboidDirection dir, final int amount) {
        switch (dir) {
            case Horizontal:
                return this.expand(CuboidDirection.North, amount)
                        .expand(CuboidDirection.South, amount)
                        .expand(CuboidDirection.East, amount)
                        .expand(CuboidDirection.West, amount);
            case Vertical:
                return this.expand(CuboidDirection.Down, amount)
                        .expand(CuboidDirection.Up, amount);
            case Both:
                return this.outset(CuboidDirection.Horizontal, amount)
                        .outset(CuboidDirection.Vertical, amount);
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    public boolean isWithin(final int x, final int z, final int radius, final String world) {
        return this.outset(CuboidDirection.Both, radius).contains(x, z, world);
    }

    public void setLocations(final Location loc1, final Location loc2) {
        this.x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
        this.x2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
        this.y1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
        this.y2 = Math.max(loc1.getBlockY(), loc2.getBlockY());
        this.z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        this.z2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }

    public Location[] getCornerLocations() {
        final World world = KRegions.getInstance().getServer().getWorld(this.world);
        return new Location[] {
                new Location(world, x1, y1, z1),
                new Location(world, x2, y1, z2),
                new Location(world, x1, y1, z2),
                new Location(world, x2, y1, z1)
        };
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Region)) return false;
        Region other = (Region) o;
        return other.getMaximumPoint().equals(this.getMaximumPoint()) &&
                other.getMinimumPoint().equals(this.getMinimumPoint());
    }

    @Override
    public int hashCode() {
        return this.getMaximumPoint().hashCode() + this.getMinimumPoint().hashCode();
    }

    @Override
    public String toString() {
        final Location corner1 = this.getMinimumPoint();
        final Location corner2 = this.getMaximumPoint();
        return corner1.getBlockX() + ":" + corner1.getBlockY() + ":" + corner1.getBlockZ() + ":" +
                corner2.getBlockX() + ":" + corner2.getBlockY() + ":" + corner2.getBlockZ() + ":" +
                this.name + ":" + this.world;
    }

    public List<Coordinate> getBorderCoordinates() {
        List<Coordinate> borderCoords = new ArrayList<>();
        Iterator<Coordinate> iterator = this.iterator();
        while (iterator.hasNext()) {
            borderCoords.add(iterator.next());
        }
        return borderCoords;
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return new BorderIterator(this.x1, this.z1, this.x2, this.z2);
    }

    public enum BorderDirection {
        POS_X, POS_Z, NEG_X, NEG_Z
    }

    public class BorderIterator implements Iterator<Coordinate> {
        private int x, z;
        private boolean next = true;
        private BorderDirection dir = BorderDirection.POS_Z;
        int maxX = getMaximumPoint().getBlockX();
        int maxZ = getMaximumPoint().getBlockZ();
        int minX = getMinimumPoint().getBlockX();
        int minZ = getMinimumPoint().getBlockZ();

        public BorderIterator(final int x1, final int z1, final int x2, final int z2) {
            this.x = Math.min(x1, x2);
            this.z = Math.min(z1, z2);
        }

        @Override
        public boolean hasNext() {
            return next;
        }

        @Override
        public Coordinate next() {
            if (dir == BorderDirection.POS_Z) {
                if (++z == maxZ) dir = BorderDirection.POS_X;
            } else if (dir == BorderDirection.POS_X) {
                if (++x == maxX) dir = BorderDirection.NEG_Z;
            } else if (dir == BorderDirection.NEG_Z) {
                if (--z == minZ) dir = BorderDirection.NEG_X;
            } else if (dir == BorderDirection.NEG_X && --x == minX) {
                next = false;
            }
            return new Coordinate(x, z);
        }

        @Override
        public void remove() {}
    }

    public enum CuboidDirection {
        North, East, South, West, Up, Down, Horizontal, Vertical, Both, Unknown
    }
}
