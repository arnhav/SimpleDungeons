package com.tyrriel.simpledungeons;

import com.tyrriel.simpledungeons.commands.*;
import com.tyrriel.simpledungeons.listeners.*;
import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.util.DungeonManager;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleDungeons extends JavaPlugin {

    public static SimpleDungeons simpleDungeons;
    public static BukkitAPIHelper bukkitAPIHelper;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading SimpleDungeons...");

        simpleDungeons = this;

        getCommand("simpledungeon").setExecutor(new SimpleDungeonCommand());

        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);

        Bukkit.getPluginManager().registerEvents(new DungeonTriggerListener(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonChestListener(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonPortalListener(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonPlayerListener(), this);

        FileManager.createConfig(this);
        FileManager.createLogFile(this);

        bukkitAPIHelper = MythicMobs.inst().getAPIHelper();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Dungeon dungeon : DungeonManager.dungeons){
            dungeon.deleteDungeonFloorIfNoMorePlayers();
        }
    }
}
