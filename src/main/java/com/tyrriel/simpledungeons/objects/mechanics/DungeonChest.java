package com.tyrriel.simpledungeons.objects.mechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;

import java.util.Objects;

public class DungeonChest {

    private Location location;
    private BlockFace facing;
    private String loottable;
    private boolean isLooted = false;

    public DungeonChest(Location location, BlockFace facing){
        setLocation(location);
        setFacing(facing);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setFacing(BlockFace facing) {
        this.facing = facing;
    }

    public void setLoottable(String loottable) {
        this.loottable = loottable;
    }

    public void setLooted(boolean looted) {
        isLooted = looted;
    }

    public Location getLocation() {
        return location;
    }

    public BlockFace getFacing() {
        return facing;
    }

    public String getLoottable() {
        return loottable;
    }

    public boolean isLooted() {
        return isLooted;
    }

    public void trigger(){
        Block block = location.getBlock();
        block.setType(Material.TRAPPED_CHEST);
        Directional chestData = (Directional) block.getBlockData();
        chestData.setFacing(facing);
        block.setBlockData(chestData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DungeonChest that = (DungeonChest) o;
        return isLooted == that.isLooted &&
                location.equals(that.location) &&
                facing == that.facing &&
                loottable.equals(that.loottable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, facing, loottable, isLooted);
    }
}
