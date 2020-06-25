package com.tyrriel.simpledungeons.objects;

import com.tyrriel.simpledungeons.util.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class DungeonBlock {

    private Material init;
    private Material triggered;
    private Location location;

    public DungeonBlock(Material init, Material triggered, Location location){
        setInit(init);
        setTriggered(triggered);
        setLocation(location);
    }

    public void setInit(Material init) {
        this.init = init;
    }

    public void setTriggered(Material triggered) {
        this.triggered = triggered;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Material getInit() {
        return init;
    }

    public Material getTriggered() {
        return triggered;
    }

    public Location getLocation() {
        return location;
    }

    public void trigger(){
        Block block = location.getBlock();
        block.setType(triggered);
        if (block.getType() == Material.END_GATEWAY) {
            BlockUtil.makeEndPortalGateway(block);
        }
    }

    @Override
    public String toString() {
        return "DungeonBlock{" +
                "init=" + init +
                ", triggered=" + triggered +
                ", location=" + location +
                '}';
    }
}
