package com.tyrriel.simpledungeons.util;

import com.sk89q.worldedit.util.Direction;
import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.objects.*;
import com.tyrriel.simpledungeons.objects.enums.RoomConfiguration;
import org.bukkit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class DungeonGenerator {

    public static HashMap<RoomConfiguration, List<String>> roomNames = new HashMap<>();

    public static void createWorld(String worldName){
        System.out.println("Creating world...");
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.generator(new EmptyChunkGenerator());

        World world = worldCreator.createWorld();
        if (world == null){
            SimpleDungeons.simpleDungeons.getLogger().severe("World Creation Error!");
            return;
        }
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.MOB_GRIEFING, false);
        System.out.println("World created...");
    }

    public static Dungeon createDungeon(String tileset, String worldName){
        File tilesetFolder = new File(FileManager.getTilesetsFolder(), tileset);
        DungeonConfiguration dungeonConfiguration = FileManager.readTilesetConfig(tilesetFolder);
        if (dungeonConfiguration == null){
            SimpleDungeons.simpleDungeons.getLogger().severe("Tileset Config File not loaded!");
            return null;
        }
        roomNames = dungeonConfiguration.getRoomNames();
        if (roomNames == null){
            SimpleDungeons.simpleDungeons.getLogger().severe("Tileset Config File error!");
            return null;
        }

        Dungeon dungeon = new Dungeon(worldName, tileset, worldName, new ArrayList<>(), new LinkedBlockingQueue<>(), dungeonConfiguration);

        String roomName = DungeonUtil.getRoomName(RoomConfiguration.START);
        DungeonRoom start = new DungeonRoom(new DungeonChunk(worldName, 0, 0), 0, RoomConfiguration.START, roomName);
        dungeon.addRoom(start);

        // North
        DungeonUtil.goInDirection(dungeon, Direction.NORTH, worldName, 0, 0, dungeonConfiguration.getPathLength(), 0, 0, tilesetFolder);
        // South
        //DungeonUtil.goInDirection(dungeon, Direction.SOUTH, worldName, 0, 0, dungeonConfiguration.getPathLength(), 0, 0, tilesetFolder);
        // East
        //DungeonUtil.goInDirection(dungeon, Direction.EAST, worldName, 0, 0, dungeonConfiguration.getPathLength(), 0, 0, tilesetFolder);
        // West
        //DungeonUtil.goInDirection(dungeon, Direction.WEST, worldName, 0, 0, dungeonConfiguration.getPathLength(), 0, 0, tilesetFolder);

        // Generate Boss Room
        /*
        ArrayList<DungeonRoom> endcaps = DungeonUtil.getEndCaps();
        DungeonRoom dungeonRoom = endcaps.get((int) (Math.random() * ((endcaps.size() - 1) + 1)));
        DungeonChunk chunk = dungeonRoom.getChunk();
        RoomConfiguration roomConfiguration = dungeonRoom.getRoomConfiguration();
        int level = dungeonRoom.getLevel();
        Direction direction = DirectionUtil.getFacing(roomConfiguration);
        DungeonChunk pasteChunk = DungeonUtil.getBossPasteChunk(chunk, direction);

        DungeonUtil.addBossRooms(dungeonRoom);
        */
        /*
        world.getChunkAtAsync(pasteChunk.getX(), pasteChunk.getZ()).thenAccept(c ->{
            pasteFile(tilesetFolder, getRoomName(RoomConfiguration.valueOf("BOSS_" + direction.toString())),
                    world, pasteChunk.getX()*16, ((level*16)), pasteChunk.getZ()*16);
        });
        */
        DungeonManager.dungeons.put(worldName, dungeon);

        System.out.println("Dungeon generation complete!");
        return dungeon;
    }

    public static void placeRooms(Dungeon dungeon){
        File tilesetFolder = new File(FileManager.getTilesetsFolder(), dungeon.getTileset());
        World world = Bukkit.getWorld(dungeon.getWorld());
        if (world == null){
            SimpleDungeons.simpleDungeons.getLogger().severe("World not created!");
            return;
        }
        System.out.println("Pasting rooms...");
        LinkedBlockingQueue<DungeonRoom> roomsToPaste = dungeon.getRoomsToPaste();
        Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, new Runnable() {
            @Override
            public void run() {
                if (roomsToPaste.isEmpty()) return;

                int count = 0;
                while (count < 10){
                    count++;
                    DungeonRoom room = roomsToPaste.poll();
                    if (room == null) continue;
                    DungeonChunk chunk = room.getChunk();
                    RoomConfiguration roomConfig = room.getRoomConfiguration();
                    int level = room.getLevel();

                    if (roomConfig.toString().contains("BOSS")) continue;

                    if (level > 0 && (DirectionUtil.doesRoomGoDown(roomConfig) || DirectionUtil.doesRoomGoUp(roomConfig))){
                        DungeonRoom dungeonRoom = DungeonUtil.getDungeonRoom(dungeon, chunk, level-1);
                        if (dungeonRoom != null && dungeonRoom.getRoomConfiguration() == roomConfig) continue;
                    }

                    String fileName = room.getRoomName();
                    if (!world.isChunkLoaded(chunk.getX(), chunk.getZ())) {
                        world.getChunkAtAsync(chunk.getX(), chunk.getZ()).thenAccept(c ->
                                DungeonUtil.pasteFile(tilesetFolder, fileName, world, c.getX() * 16, ((level * 16)), c.getZ() * 16));
                    } else {
                        DungeonUtil.pasteFile(tilesetFolder, fileName, world, chunk.getX() * 16, ((level * 16)), chunk.getZ() * 16);
                    }

                    System.out.println(dungeon.getRooms().indexOf(room) + "/" + (dungeon.getRooms().size()-1) + " completed...");
                }

                Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, this, 10);
            }
        }, 0);
    }
}
