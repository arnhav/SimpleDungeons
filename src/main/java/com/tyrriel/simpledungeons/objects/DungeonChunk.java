package com.tyrriel.simpledungeons.objects;

import org.bukkit.World;

import java.util.Objects;

public class DungeonChunk {

    private String world;
    private int x, z;

    public DungeonChunk(String world, int x, int z){
        setWorld(world);
        setX(x);
        setZ(z);
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DungeonChunk that = (DungeonChunk) o;
        return x == that.x &&
                z == that.z &&
                world.equals(that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }

    @Override
    public String toString() {
        return "DungeonChunk{" +
                "world='" + world + '\'' +
                ", x=" + x +
                ", z=" + z +
                '}';
    }
}
