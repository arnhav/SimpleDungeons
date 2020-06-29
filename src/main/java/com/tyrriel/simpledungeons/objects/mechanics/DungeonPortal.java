package com.tyrriel.simpledungeons.objects.mechanics;

import com.tyrriel.simpledungeons.util.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class DungeonPortal {

    private Location location;

    public DungeonPortal(Location location){
        setLocation(location);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void trigger(){
        Block block = location.getBlock();
        block.setType(Material.END_GATEWAY);
        BlockUtil.makeEndPortalGateway(block);
    }
}
