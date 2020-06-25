package com.tyrriel.simpledungeons.objects;

import com.tyrriel.simpledungeons.SimpleDungeons;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class DungeonDoor {

    private Location point1, point2;
    private Material doorMaterial;
    private String name;

    private boolean isOpen = false;

    public DungeonDoor(String name){
        setName(name);
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
        World world = point1.getWorld();
        int minx = Math.min(point1.getBlockX(), point2.getBlockX());
        int maxx = Math.max(point1.getBlockX(), point2.getBlockX());
        int minz = Math.min(point1.getBlockZ(), point2.getBlockZ());
        int maxz = Math.max(point1.getBlockZ(), point2.getBlockZ());
        int miny = Math.min(point1.getBlockY(), point2.getBlockY());
        int maxy = Math.max(point1.getBlockY(), point2.getBlockY());

        for (int x = minx; x < maxx; x++){
            for (int z = minz; z < maxz; z++){
                for (int y = miny; y < maxy; y++){
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(doorMaterial);
                }
            }
        }
    }

    public void openDoor() {
        World world = point1.getWorld();
        int minx = Math.min(point1.getBlockX(), point2.getBlockX());
        int maxx = Math.max(point1.getBlockX(), point2.getBlockX());
        int minz = Math.min(point1.getBlockZ(), point2.getBlockZ());
        int maxz = Math.max(point1.getBlockZ(), point2.getBlockZ());
        int miny = Math.min(point1.getBlockY(), point2.getBlockY());
        int maxy = Math.max(point1.getBlockY(), point2.getBlockY());

        for (int y = miny; y < maxy; y++){
            int finalY = y;
            Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, ()->{
                for (int x = minx; x < maxx; x++){
                    for (int z = minz; z < maxz; z++){
                        Block block = world.getBlockAt(x, finalY, z);
                        block.setType(Material.AIR);
                    }
                }
            }, 20);
        }
        setOpen(true);
    }
}
