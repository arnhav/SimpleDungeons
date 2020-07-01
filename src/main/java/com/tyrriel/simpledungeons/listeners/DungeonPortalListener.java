package com.tyrriel.simpledungeons.listeners;

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.objects.instance.DungeonGroup;
import com.tyrriel.simpledungeons.objects.instance.DungeonPlayer;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.EndGateway;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DungeonPortalListener implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEndGatewayEvent event){
        Player player = event.getPlayer();
        EndGateway endGateway = event.getGateway();

        if (!DungeonManager.isDungeonWorld(endGateway.getWorld())) return;

        DungeonPlayer dp = DungeonManager.dungeonPlayers.get(player);
        if (dp == null) return;
        event.setCancelled(true);

        Location location = dp.getLocation();
        player.teleport(location);

        DungeonGroup group = DungeonManager.getDungeonGroup(player);
        if (group == null) return;
        group.removePlayer(dp);

        if (group.getPlayers().isEmpty()) {
            Dungeon dungeon = group.getDungeon();
            Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, () -> {
                if (dungeon.deleteDungeonFloorIfNoMorePlayers())
                    dungeon.createDungeonFloor();
            }, 20);
        }
    }

}
