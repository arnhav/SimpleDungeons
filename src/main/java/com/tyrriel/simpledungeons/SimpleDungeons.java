package com.tyrriel.simpledungeons;

import com.tyrriel.simpledungeons.commands.*;
import com.tyrriel.simpledungeons.listeners.*;
import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.tasks.DeadPlayerTask;
import com.tyrriel.simpledungeons.tasks.DungeonFloorCreationTask;
import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.objects.DungeonFloor;
import com.tyrriel.simpledungeons.objects.instance.DungeonPlayer;
import com.tyrriel.simpledungeons.util.DungeonManager;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleDungeons extends JavaPlugin {

    public static SimpleDungeons simpleDungeons;
    public static BukkitAPIHelper bukkitAPIHelper;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading SimpleDungeons...");

        simpleDungeons = this;

        FileManager.createConfig(this);
        FileManager.createItemsFile(this);
        FileManager.createLogFile(this);

        getCommand("simpledungeon").setExecutor(new SimpleDungeonCommand());

        Bukkit.getPluginManager().registerEvents(new WorldCreationListener(), this);

        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);

        Bukkit.getPluginManager().registerEvents(new DungeonTriggerListener(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonChestListener(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonPortalListener(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonPlayerListener(), this);

        new DeadPlayerTask(this);
        new DungeonFloorCreationTask(this);

        bukkitAPIHelper = MythicMobs.inst().getAPIHelper();

        final int[] i = {0};
        Bukkit.getScheduler().runTaskLater(simpleDungeons, new Runnable() {
            @Override
            public void run() {
                if (DungeonManager.dungeons.size() <= i[0]) return;
                Dungeon dungeon = DungeonManager.dungeons.get(i[0]);
                DungeonFloor df = DungeonManager.createDungeonFloorWorld(dungeon, 0);
                dungeon.setDungeonFloor(df);
                i[0]++;
                Bukkit.getScheduler().runTaskLater(simpleDungeons, this, 15*20);
            }
        }, 10*20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : DungeonManager.dungeonPlayers.keySet()){
            DungeonPlayer dp = DungeonManager.dungeonPlayers.get(player);
            player.teleport(dp.getLocation());
        }
        for (Dungeon dungeon : DungeonManager.dungeons){
            DungeonManager.deleteDungeonFloorWorld(dungeon);
        }
    }
}
