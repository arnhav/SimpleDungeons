package com.tyrriel.simpledungeons.monitors;

import com.tyrriel.simpledungeons.objects.DungeonConfiguration;
import com.tyrriel.simpledungeons.objects.DungeonFloor;
import com.tyrriel.simpledungeons.objects.generation.DungeonFloorConfiguration;
import com.tyrriel.simpledungeons.util.DungeonGenerator;
import com.tyrriel.simpledungeons.util.DungeonManager;
import com.tyrriel.simpledungeons.util.RandomUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.LinkedBlockingQueue;

public class DungeonFloorCreationTask implements Runnable {

    public DungeonFloorCreationTask(JavaPlugin plugin){
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 0, 10*20);
    }

    @Override
    public void run() {
        for (DungeonConfiguration dc : DungeonManager.dungeonConfigurations.values()){
            String tileset = dc.getTileSets().get(RandomUtil.randomWithRange(0, dc.getTileSets().size()-1));
            LinkedBlockingQueue<DungeonFloor> floors = DungeonManager.preparedDungeonFloors.getOrDefault(dc, new LinkedBlockingQueue<>());
            while (floors.size() < 12){
                DungeonFloor df = DungeonGenerator.generateDungeon(tileset, tileset + floors.size());
                if (df == null) return;
                DungeonFloorConfiguration dfc = df.getDungeonFloorConfiguration();
                dfc.setBoss(dc.getBosses().get(RandomUtil.randomWithRange(0, dc.getBosses().size()-1)));
                dfc.setMobs(dc.getMobSets().get(RandomUtil.randomWithRange(0, dc.getMobSets().size()-1)));
                df.setDungeonFloorConfiguration(dfc);
                floors.add(df);
            }
            DungeonManager.preparedDungeonFloors.put(dc, floors);
        }
    }
}
