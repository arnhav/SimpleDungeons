package com.tyrriel.simpledungeons.objects.mechanics;

import com.tyrriel.simpledungeons.SimpleDungeons;
import org.bukkit.*;
import org.bukkit.block.Block;

public class DungeonDoor {

    private Location point1, point2;
    private Material doorMaterial;
    private String name;
    private World world;

    private boolean isOpen = false;

    public DungeonDoor(String name, World world){
        setName(name);
        setWorld(world);
    }

    public void setPoint1(Location point1) {
        this.point1 = point1;
    }

    public void setPoint2(Location point2) {
        this.point2 = point2;
    }

    public void setDoorMaterial(Material doorMaterial) {
        this.doorMaterial = doorMaterial;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Location getPoint1() {
        return point1;
    }

    public Location getPoint2() {
        return point2;
    }

    public Material getDoorMaterial() {
        return doorMaterial;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void fillDoor() {
        if (point1 == null) return;
        if (point2 == null) return;
        int minx = Math.min(point1.getBlockX(), point2.getBlockX());
        int maxx = Math.max(point1.getBlockX(), point2.getBlockX());

        int minz = Math.min(point1.getBlockZ(), point2.getBlockZ());
        int maxz = Math.max(point1.getBlockZ(), point2.getBlockZ());

        int miny = Math.min(point1.getBlockY(), point2.getBlockY());
        int maxy = Math.max(point1.getBlockY(), point2.getBlockY());

        for (int y = miny; y <= maxy; y++){
            for (int x = minx; x <= maxx; x++){
                for (int z = minz; z <= maxz; z++){
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(doorMaterial);
                }
            }
        }
    }

    public void openDoor() {
        int minx = Math.min(point1.getBlockX(), point2.getBlockX());
        int maxx = Math.max(point1.getBlockX(), point2.getBlockX());

        int minz = Math.min(point1.getBlockZ(), point2.getBlockZ());
        int maxz = Math.max(point1.getBlockZ(), point2.getBlockZ());

        int miny = Math.min(point1.getBlockY(), point2.getBlockY());
        int maxy = Math.max(point1.getBlockY(), point2.getBlockY());

        final int[] y = {miny};
        Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, new Runnable() {
            @Override
            public void run() {
                if (y[0] > maxy) return;
                for (int x = minx; x <= maxx; x++){
                    for (int z = minz; z <= maxz; z++){
                        Block block = world.getBlockAt(x, y[0], z);
                        Location location = block.getLocation();
                        block.setType(Material.AIR);
                        world.spawnParticle(Particle.CLOUD, location, 1, 0, 0, 0);
                    }
                }
                Location sound = world.getBlockAt(((minx+maxx)/2), ((miny+maxy)/2), ((minz+maxz)/2)).getLocation();
                world.playSound(sound, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 3f, 0.5f);
                y[0]++;
                Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, this, 20);
            }
        }, 20);
        setOpen(true);
    }
}
