package com.tyrriel.simpledungeons.util;

import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.objects.*;
import com.tyrriel.simpledungeons.objects.instance.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class DungeonManager {

    public static HashMap<DungeonConfiguration, LinkedBlockingQueue<DungeonFloor>> preparedDungeonFloors = new HashMap<>();

    public static HashMap<String, DungeonConfiguration> dungeonConfigurations = new HashMap<>();
    public static HashMap<DungeonConfiguration, List<DungeonGroup>> dungeonGroups = new HashMap<>();

    public static HashMap<Player, DungeonPlayer> dungeonPlayers = new HashMap<>();

    public static List<Dungeon> dungeons = new ArrayList<>();

    public static HashMap<String, ItemStack> dungeonItems =  new HashMap<>();

    public static DungeonFloor createDungeonFloorWorld(Dungeon dungeon, int floor){
        DungeonConfiguration dc = dungeon.getDungeonConfiguration();
        DungeonGroup dg = dungeon.getDungeonGroup();
        String worldName = "SD_" + dc.getId() + "_" + floor + "_" + DungeonManager.dungeonGroups.get(dc).indexOf(dg);
        DungeonGenerator.createWorld(worldName);
        DungeonFloor df = preparedDungeonFloors.get(dc).poll();
        if (df == null) return null;
        df.setName(worldName);
        df.setWorld(worldName);
        DungeonGenerator.placeRooms(df);
        return df;
    }

    public static boolean deleteDungeonFloorWorld(Dungeon dungeon){
        DungeonFloor df = dungeon.getDungeonFloor();
        DungeonGroup dg = dungeon.getDungeonGroup();
        World world = Bukkit.getWorld(df.getWorld());
        if (world == null) return false;
        if (world.getPlayerCount() > 0) return false;
        Bukkit.unloadWorld(world, false);
        File worldFile = new File(Bukkit.getWorldContainer(), df.getWorld());
        FileManager.deleteDirectory(worldFile);
        System.out.println("Dungeon " + df.getName() + " deleted.");
        dg.setPlayingDungeon(false);
        return true;
    }

    public static boolean isTileSetPresent(String tileset) {
        File[] files = FileManager.getTilesetsFolder().listFiles();
        for (File file : files){
            if (file.getName().equalsIgnoreCase(tileset))
                return true;
        }
        return false;
    }

    public static boolean isDungeonWorld(World world) {
        return getDungeonFloor(world) != null;
    }

    public static DungeonFloor getDungeonFloor(World world) {
        for (Dungeon dungeon : dungeons){
            DungeonFloor dungeonFloor = dungeon.getDungeonFloor();
            if (dungeonFloor == null) continue;
            if (world.getName().equals(dungeonFloor.getName()))
                return dungeonFloor;
        }
        return null;
    }

    public static DungeonGroup getDungeonGroup(Player player){
        for (List<DungeonGroup> groups : dungeonGroups.values()){
            for (DungeonGroup group : groups){
                for (DungeonPlayer dp : group.getPlayers()){
                    if (player.equals(dp.getPlayer()))
                        return group;
                }
            }
        }
        return null;
    }

    public static boolean areAllPlayersReady(DungeonGroup dg){
        for (DungeonPlayer dp : dg.getPlayers()){
            if (dp.isReady()) continue;
            return false;
        }
        return true;
    }

}
