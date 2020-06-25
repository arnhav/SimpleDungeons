package com.tyrriel.simpledungeons.listeners;

import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.loot.LootTables;

public class DungeonChestListener implements Listener {

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event){
        Block block = event.getClickedBlock();

        if (block == null) return;

        if (!(block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.CHEST)) return;

        if (!DungeonManager.isDungeonWorld(block.getWorld())) return;

        Dungeon dungeon = DungeonManager.getDungeon(block.getWorld());

        if (dungeon == null) return;

        Chest chest = (Chest) block.getState();

        if (!dungeon.getOpenedChest().contains(chest)) {
            chest.setLootTable(LootTables.END_CITY_TREASURE.getLootTable());
            dungeon.setChestToOpened(chest);
            chest.update();
        }
    }

}
