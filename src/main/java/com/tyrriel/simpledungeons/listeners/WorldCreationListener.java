package com.tyrriel.simpledungeons.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class WorldCreationListener implements Listener {

    @EventHandler
    public void onWorldInit(WorldInitEvent event){
        World world = event.getWorld();
        if (!world.getName().contains("SD")) return;
        world.setKeepSpawnInMemory(false);
    }

}
