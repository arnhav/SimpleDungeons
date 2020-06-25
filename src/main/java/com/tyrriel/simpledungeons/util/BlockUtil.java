package com.tyrriel.simpledungeons.util;

import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;

public class BlockUtil {

    public static void makeEndPortalGateway(Block block){
        EndGateway eg = (EndGateway) block.getState();
        eg.setAge(Long.MAX_VALUE);
        eg.setExitLocation(block.getLocation());
        eg.update();
    }

}
