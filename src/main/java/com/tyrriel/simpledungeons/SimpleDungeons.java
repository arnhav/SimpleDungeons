package com.tyrriel.simpledungeons;

import com.tyrriel.simpledungeons.commands.*;
import com.tyrriel.simpledungeons.listeners.DungeonListener;
import com.tyrriel.simpledungeons.data.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleDungeons extends JavaPlugin {

    public static SimpleDungeons simpleDungeons;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading SimpleDungeons...");

        simpleDungeons = this;

        getCommand("simpledungeon").setExecutor(new SimpleDungeonCommand());

        Bukkit.getPluginManager().registerEvents(new DungeonListener(), this);

        FileManager.createConfig(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
