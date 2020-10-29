package com.tyrriel.simpledungeons.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DungeonPlayerScoreboardTask implements Runnable {

    public DungeonPlayerScoreboardTask(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 20);
    }

    @Override
    public void run() {

    }
}
