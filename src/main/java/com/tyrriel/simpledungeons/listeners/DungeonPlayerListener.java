package com.tyrriel.simpledungeons.listeners;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.objects.DungeonFloor;
import com.tyrriel.simpledungeons.objects.instance.DungeonGroup;
import com.tyrriel.simpledungeons.objects.instance.DungeonPlayer;
import com.tyrriel.simpledungeons.util.DungeonInstanceUtil;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DungeonPlayerListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        if (!(attacker instanceof Player)) {
            if (attacker instanceof Projectile) {
                attacker = (Entity) ((Projectile) attacker).getShooter();
                if (!(attacker instanceof Player)) return;
            } else {
                return;
            }
        }
        if (!(victim instanceof Player)) return;

        if (!DungeonManager.isDungeonWorld(attacker.getWorld())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        PlayerTeleportEvent.TeleportCause teleportCause = event.getCause();

        DungeonPlayer dp = DungeonManager.dungeonPlayers.get(player);
        if (dp == null) return;
        if (!dp.isDead()) return;
        if (teleportCause != PlayerTeleportEvent.TeleportCause.SPECTATE) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onStopSpectating(PlayerStopSpectatingEntityEvent event){
        Player player = event.getPlayer();

        DungeonPlayer dp = DungeonManager.dungeonPlayers.get(player);
        if (dp == null) return;
        if (!dp.isDead()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();

        DungeonPlayer dp = DungeonManager.dungeonPlayers.get(player);
        if (dp == null) return;

        List<Entity> nearby = player.getNearbyEntities(1, 1, 1);

        if (nearby.isEmpty()) return;

        if (!DungeonInstanceUtil.containsRespawnEntity(nearby)) return;

        for (Entity e : nearby){
            if (!e.getScoreboardTags().contains("RESPAWN")) continue;
            PersistentDataContainer pdc = e.getPersistentDataContainer();
            String uuid = pdc.get(DungeonInstanceUtil.respawnPlayerKey, PersistentDataType.STRING);
            Player dead = Bukkit.getPlayer(UUID.fromString(uuid));
            if (dead == null) continue;
            e.remove();
            DungeonPlayer ddp = DungeonManager.dungeonPlayers.get(dead);
            dead.setGameMode(GameMode.ADVENTURE);
            dead.setFlySpeed(0.2f);
            dead.setWalkSpeed(0.2f);
            ddp.setDead(false);
            break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(EntityDeathEvent event){
        LivingEntity le = event.getEntity();

        if (!(le instanceof Player)) return;

        Player player = (Player) le;

        DungeonPlayer dp = DungeonManager.dungeonPlayers.get(player);
        if (dp == null) return;

        if (event.isCancelled()) return;

        event.setCancelled(true);

        DungeonInstanceUtil.makePlayerDead(player);
        dp.setDead(true);
        player.sendTitle("", ChatColor.DARK_RED + "" + ChatColor.BOLD + "You Died", 10, 60, 10);

        DungeonGroup group = DungeonManager.getDungeonGroup(player);
        if (group == null) return;

        DungeonInstanceUtil.sendAllPlayersInGroupMessage(group, ChatColor.GRAY + player.getName() + " has fallen!");

        if (DungeonInstanceUtil.areAllPlayersInGroupDead(group)){
            DungeonInstanceUtil.sendAllPlayersInGroupTitle(group, ChatColor.DARK_RED + "☠", "All Party Members Died");
            DungeonInstanceUtil.sendAllPlayersInGroupMessage(group, ChatColor.GRAY + "Leaving in 5 sec...");
            Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, ()->{
                List<DungeonPlayer> list = new ArrayList<>(group.getPlayers());
                for (DungeonPlayer dungeonPlayer : group.getPlayers()){
                    Player temp = dungeonPlayer.getPlayer();
                    temp.setGameMode(GameMode.SURVIVAL);
                    temp.setFlySpeed(0.2f);
                    temp.setWalkSpeed(0.2f);
                    temp.teleport(dungeonPlayer.getLocation());
                }
                for (DungeonPlayer p : list){
                    group.removePlayer(p);
                    DungeonManager.dungeonPlayers.remove(p.getPlayer());
                }

                DungeonManager.deleteDungeonFloorWorld(group.getDungeon());
                DungeonFloor df = DungeonManager.createDungeonFloorWorld(group.getDungeon(), 0);
                group.getDungeon().setDungeonFloor(df);
            }, 5*20);
        }
    }

}
