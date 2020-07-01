package com.tyrriel.simpledungeons.util;

import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.objects.*;
import com.tyrriel.simpledungeons.objects.instance.*;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DungeonManager {

    public static HashMap<String, DungeonConfiguration> dungeonConfigurations = new HashMap<>();
    public static HashMap<DungeonConfiguration, List<DungeonGroup>> dungeonGroups = new HashMap<>();

    public static HashMap<Player, DungeonPlayer> dungeonPlayers = new HashMap<>();

    public static List<Dungeon> dungeons = new ArrayList<>();

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
