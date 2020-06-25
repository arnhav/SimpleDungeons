package com.tyrriel.simpledungeons.util;

import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.objects.DungeonPlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DungeonManager {

    public static HashMap<String, Dungeon> dungeons = new HashMap<>();

    public static HashMap<Player, DungeonPlayer> dungeonPlayers = new HashMap<>();

    public static boolean isDungeonWorld(World world){
        return getDungeon(world) != null;
    }

    public static Dungeon getDungeon(World world){
        for (Dungeon dungeon : dungeons.values()){
            if (world.getName().equals(dungeon.getName()))
                return dungeon;
        }
        return null;
    }

}
