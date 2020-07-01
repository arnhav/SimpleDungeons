package com.tyrriel.simpledungeons.listeners;

import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.objects.instance.DungeonGroup;
import com.tyrriel.simpledungeons.objects.instance.DungeonPlayer;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class DungeonPlayerListener implements Listener {

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event){
        LivingEntity le = event.getEntity();

        if (!(le instanceof Player)) return;

        Player player = (Player) le;

        DungeonPlayer dp = DungeonManager.dungeonPlayers.get(player);
        if (dp == null) return;

        event.setCancelled(true);

        // TODO: Put the player into a respawning state here

        DungeonGroup group = DungeonManager.getDungeonGroup(player);
        if (group == null) return;

        Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, ()->{
            player.teleport(group.getDungeon().getDungeonFloor().getStart());
        }, 1);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        if (!(attacker instanceof Player)) return;
        if (!(victim instanceof Player)) return;

        if (!DungeonManager.isDungeonWorld(attacker.getWorld())) return;

        event.setCancelled(true);
    }

}
