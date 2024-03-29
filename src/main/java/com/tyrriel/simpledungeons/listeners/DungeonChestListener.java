package com.tyrriel.simpledungeons.listeners;

import com.tyrriel.simpledungeons.objects.DungeonFloor;
import com.tyrriel.simpledungeons.objects.mechanics.DungeonChest;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DungeonChestListener implements Listener {

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event){
        Block block = event.getClickedBlock();

        if (block == null) return;

        if (!(block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.CHEST)) return;

        if (!DungeonManager.isDungeonWorld(block.getWorld())) return;

        DungeonFloor dungeonFloor = DungeonManager.getDungeonFloor(block.getWorld());

        if (dungeonFloor == null) return;

        if (!dungeonFloor.getChests().containsKey(block.getLocation())) return;

        DungeonChest dc = dungeonFloor.getChests().get(block.getLocation());


    }

}
