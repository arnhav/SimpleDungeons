package com.tyrriel.simpledungeons;

import com.tyrriel.simpledungeons.commands.*;
import com.tyrriel.simpledungeons.listeners.DungeonChestListener;
import com.tyrriel.simpledungeons.listeners.DungeonTriggerListener;
import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.listeners.DungeonMobSpawnListener;
import com.tyrriel.simpledungeons.listeners.DungeonPortalListener;
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

        Bukkit.getPluginManager().registerEvents(new DungeonTriggerListener(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonChestListener(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonMobSpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonPortalListener(), this);

        FileManager.createConfig(this);
        FileManager.createLogFile(this);

        bukkitAPIHelper = MythicMobs.inst().getAPIHelper();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
