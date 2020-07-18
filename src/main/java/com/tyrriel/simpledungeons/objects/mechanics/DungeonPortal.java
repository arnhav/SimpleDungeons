package com.tyrriel.simpledungeons.objects.mechanics;

import com.tyrriel.simpledungeons.objects.enums.PortalType;
import com.tyrriel.simpledungeons.util.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class DungeonPortal {

    private Location location;
    private PortalType portalType;

    public DungeonPortal(Location location, PortalType portalType){
        setLocation(location);
        setPortalType(portalType);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPortalType(PortalType portalType) {
        this.portalType = portalType;
    }

    public Location getLocation() {
        return location;
    }

    public PortalType getPortalType() {
        return portalType;
    }

    public void trigger(){
        Block block = location.getBlock();
        block.setType(Material.END_GATEWAY);
        BlockUtil.makeEndPortalGateway(block);
    }
}
