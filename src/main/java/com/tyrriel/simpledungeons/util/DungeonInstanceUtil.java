package com.tyrriel.simpledungeons.util;

import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.gui.GUIItems;
import com.tyrriel.simpledungeons.objects.instance.DungeonGroup;
import com.tyrriel.simpledungeons.objects.instance.DungeonPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class DungeonInstanceUtil {

    public static NamespacedKey respawnPlayerKey = new NamespacedKey(SimpleDungeons.simpleDungeons, "PLAYER");

    public static void makePlayerDead(Player player){
        Skeleton skeleton = (Skeleton) player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON);
        skeleton.setAI(false);
        skeleton.setGravity(false);
        skeleton.setInvulnerable(true);
        skeleton.setCustomNameVisible(true);
        skeleton.clearLootTable();
        skeleton.setCustomName(ChatColor.translateAlternateColorCodes('&', "[&4☠&r] " + player.getName()));

        skeleton.addScoreboardTag("RESPAWN");

        PersistentDataContainer pdc = skeleton.getPersistentDataContainer();
        pdc.set(respawnPlayerKey, PersistentDataType.STRING, player.getUniqueId().toString());

        skeleton.getEquipment().setHelmet(GUIItems.getPlayerHead(player, ""));
        skeleton.getEquipment().setChestplate(player.getEquipment().getChestplate());
        skeleton.getEquipment().setLeggings(player.getEquipment().getLeggings());
        skeleton.getEquipment().setBoots(player.getEquipment().getBoots());
        skeleton.getEquipment().setItemInMainHand(player.getEquipment().getItemInMainHand());
        skeleton.getEquipment().setItemInOffHand(player.getEquipment().getItemInOffHand());

        player.setGameMode(GameMode.SPECTATOR);
        player.setFlySpeed(0);
        player.setWalkSpeed(0);
        player.setSpectatorTarget(skeleton);
    }

    public static boolean areAllPlayersInGroupDead(DungeonGroup group){
        for (DungeonPlayer dp : group.getPlayers()){
            if (!dp.isDead()) return false;
        }
        return true;
    }

    public static void sendAllPlayersInGroupTitle(DungeonGroup group, String title, String subTitle){
        for (DungeonPlayer dp : group.getPlayers()){
            Player player = dp.getPlayer();
            player.sendTitle(title, subTitle, 10, 100, 10);
        }
    }

    public static void sendAllPlayersInGroupMessage(DungeonGroup group, String message){
        for (DungeonPlayer dp : group.getPlayers()){
            Player player = dp.getPlayer();
            player.sendMessage(message);
        }
    }

    public static boolean containsRespawnEntity(List<Entity> entities){
        for (Entity entity : entities){
            if (entity.getScoreboardTags().contains("RESPAWN")) return true;
        }
        return false;
    }

}
