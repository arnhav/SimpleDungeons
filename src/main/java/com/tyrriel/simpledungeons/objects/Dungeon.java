package com.tyrriel.simpledungeons.objects;

import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.objects.instance.DungeonGroup;
import com.tyrriel.simpledungeons.util.DungeonGenerator;
import com.tyrriel.simpledungeons.util.DungeonManager;
import com.tyrriel.simpledungeons.util.RandomUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;

public class Dungeon {

    private DungeonConfiguration dungeonConfiguration;
    private DungeonFloor dungeonFloor;
    private DungeonGroup dungeonGroup;

    public Dungeon(DungeonConfiguration dungeonConfiguration){
        setDungeonConfiguration(dungeonConfiguration);
    }

    public void setDungeonConfiguration(DungeonConfiguration dungeonConfiguration) {
        this.dungeonConfiguration = dungeonConfiguration;
    }

    public void setDungeonFloor(DungeonFloor dungeonFloor) {
        this.dungeonFloor = dungeonFloor;
    }

    public void setDungeonGroup(DungeonGroup dungeonGroup) {
        this.dungeonGroup = dungeonGroup;
    }

    public DungeonConfiguration getDungeonConfiguration() {
        return dungeonConfiguration;
    }

    public DungeonFloor getDungeonFloor() {
        return dungeonFloor;
    }

    public DungeonGroup getDungeonGroup() {
        return dungeonGroup;
    }

    public DungeonFloor createDungeonFloor() {
        String worldName = "SD_" + dungeonConfiguration.getId() + "_" + DungeonManager.dungeonGroups.get(dungeonConfiguration).indexOf(dungeonGroup);
        String tileset = dungeonConfiguration.getTilesets().get(RandomUtil.randomWithRange(0, dungeonConfiguration.getTilesets().size()-1));
        DungeonGenerator.createWorld(worldName);
        Bukkit.getScheduler().runTaskAsynchronously(SimpleDungeons.simpleDungeons, ()->{
            dungeonFloor = DungeonGenerator.generateDungeon(tileset, worldName);
        });
        Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, ()->{
            DungeonGenerator.placeRooms(dungeonFloor);
        }, 2*20);
        setDungeonFloor(dungeonFloor);
        return dungeonFloor;
    }

    public boolean deleteDungeonFloorIfNoMorePlayers(){
        World world = Bukkit.getWorld(dungeonFloor.getName());
        if (world == null) return false;
        if (world.getPlayerCount() > 0) return false;
        Bukkit.unloadWorld(world, false);
        File worldFile = new File(Bukkit.getWorldContainer(), dungeonFloor.getName());
        FileManager.deleteDirectory(worldFile);
        System.out.println("Dungeon " + dungeonFloor.getName() + " deleted.");
        dungeonGroup.setPlayingDungeon(false);
        return true;
    }
}
