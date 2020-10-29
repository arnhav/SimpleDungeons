package com.tyrriel.simpledungeons.tasks;

import com.tyrriel.simpledungeons.objects.instance.DungeonPlayer;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DeadPlayerTask implements Runnable {

    public DeadPlayerTask(JavaPlugin plugin){
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 20);
    }

    @Override
    public void run() {
        for (Player player : DungeonManager.dungeonPlayers.keySet()){
            DungeonPlayer dp = DungeonManager.dungeonPlayers.get(player);
            if (!dp.isDead()) continue;
            player.sendActionBar(ChatColor.RED + "Waiting to be respawned...");
        }
    }
}
