package com.tyrriel.simpledungeons.listeners;

import com.tyrriel.simpledungeons.objects.*;
import com.tyrriel.simpledungeons.objects.enums.TriggerType;
import com.tyrriel.simpledungeons.objects.instance.DungeonPlayer;
import com.tyrriel.simpledungeons.objects.mechanics.DungeonBoss;
import com.tyrriel.simpledungeons.objects.mechanics.DungeonDoor;
import com.tyrriel.simpledungeons.objects.mechanics.DungeonTrigger;
import com.tyrriel.simpledungeons.util.DungeonManager;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DungeonTriggerListener implements Listener {

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event){
        if (!DungeonManager.isDungeonWorld(event.getEntity().getWorld())) return;
        DungeonFloor dungeonFloor = DungeonManager.getDungeonFloor(event.getEntity().getWorld());
        if (dungeonFloor == null) return;
        ActiveMob am = event.getMob();
        DungeonBoss db = dungeonFloor.getDungeonBoss();
        DungeonTrigger dt;
        if (db != null && am.getMobType().equalsIgnoreCase(db.getType())){
            dt = new DungeonTrigger(TriggerType.BOSS);
        } else {
            dt = new DungeonTrigger(TriggerType.MOB, am.getMobType());
        }
        dungeonFloor.trigger(dt);
    }

    @EventHandler
    public void onOpenDoor(PlayerInteractEvent event){
        Player player = event.getPlayer();
        DungeonPlayer dp = DungeonManager.dungeonPlayers.get(player);
        if (dp == null) return;
        DungeonFloor dungeonFloor = DungeonManager.getDungeonFloor(player.getWorld());
        if (dungeonFloor == null) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        DungeonDoor dd = dungeonFloor.getDungeonDoor(block.getLocation());
        if (dd == null) return;
        if (dd.isOpen()) return;
        if (!dd.getKey().equalsIgnoreCase("")){
            // TODO: Check the player for the key
            return;
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 3f, 1.5f);
        dd.openDoor();
        DungeonTrigger dt = new DungeonTrigger(TriggerType.DOOR, dd.getName());
        dungeonFloor.trigger(dt);
    }

}
