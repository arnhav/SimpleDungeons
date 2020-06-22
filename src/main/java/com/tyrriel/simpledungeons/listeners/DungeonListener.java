package com.tyrriel.simpledungeons.listeners;

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.EndGateway;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.loot.LootTables;


public class DungeonListener implements Listener {

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event){

        Block block = event.getClickedBlock();

        if (block == null) return;

        if (!(block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.CHEST)) return;

        if (!DungeonManager.isDungeonWorld(block.getWorld())) return;

        Dungeon dungeon = DungeonManager.getDungeon(block.getWorld());

        DoubleChest doubleChest = (DoubleChest) ((Chest) block.getState()).getInventory().getHolder();
        Chest left = (Chest) doubleChest.getLeftSide();
        Chest right = (Chest) doubleChest.getRightSide();

        if (block.getType() == Material.CHEST){
            if (!dungeon.isBossDefeated()){
                event.setUseInteractedBlock(Event.Result.DENY);
            }
        }

        if (!dungeon.getOpenedChest().contains(left) || !dungeon.getOpenedChest().contains(right)) {
            left.setLootTable(LootTables.END_CITY_TREASURE.getLootTable());
            right.setLootTable(LootTables.END_CITY_TREASURE.getLootTable());
            dungeon.setChestToOpened(left);
            dungeon.setChestToOpened(right);
            left.update();
            right.update();
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEndGatewayEvent event){
        Player player = event.getPlayer();
        EndGateway endGateway = event.getGateway();

        if (!DungeonManager.isDungeonWorld(endGateway.getWorld())) return;

        if (!DungeonManager.playersInDungeon.containsKey(event.getPlayer())) return;

        event.setCancelled(true);

        Location location = DungeonManager.playerLocations.get(player);

        player.teleport(location);

        DungeonManager.playersInDungeon.remove(player);
        DungeonManager.playerLocations.remove(player);
    }

}
