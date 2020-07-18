package com.tyrriel.simpledungeons.util;

import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.objects.*;
import com.tyrriel.simpledungeons.objects.generation.DungeonChunk;
import com.tyrriel.simpledungeons.objects.generation.DungeonFloorConfiguration;
import com.tyrriel.simpledungeons.objects.generation.DungeonRoom;
import com.tyrriel.simpledungeons.objects.generation.RoomConfiguration;
import org.bukkit.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class DungeonGenerator {

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
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        System.out.println("World created...");
    }

    public static DungeonFloor generateDungeon(String tileset, String worldName){
        File tilesetFolder = new File(FileManager.getTilesetsFolder(), tileset);
        DungeonFloorConfiguration dungeonFloorConfiguration = FileManager.readTilesetConfig(tilesetFolder);
        if (dungeonFloorConfiguration == null){
            SimpleDungeons.simpleDungeons.getLogger().severe("Tileset Config File not loaded!");
            return null;
        }
        List<RoomConfiguration> rooms = dungeonFloorConfiguration.getRooms();
        if (rooms == null || rooms.isEmpty()){
            SimpleDungeons.simpleDungeons.getLogger().severe("Tileset Config File error!");
            return null;
        }

        DungeonFloor dungeonFloor = new DungeonFloor(worldName, tileset, worldName, dungeonFloorConfiguration);

        RoomConfiguration roomConfig = RoomConfigurationUtil.findAndSelectStartRoom(rooms);
        if (roomConfig == null){
            SimpleDungeons.simpleDungeons.getLogger().severe("No Start Room defined!");
            return null;
        }
        DungeonChunk startChunk = new DungeonChunk(worldName, 0, 0, 0);
        List<DungeonChunk> chunks = RoomConfigurationUtil.getChunksForRoomConfiguration(roomConfig, startChunk);
        DungeonRoom start = new DungeonRoom(chunks, roomConfig);
        dungeonFloor.addRoom(start);

        DungeonUtil.generateNextRoom(dungeonFloor, start, 0);
        DungeonUtil.generateBossRoom(dungeonFloor);
        DungeonUtil.generateEndCaps(dungeonFloor);

        dungeonFloor.setDungeonFloorConfiguration(dungeonFloorConfiguration);
        return dungeonFloor;
    }

    public static void placeRooms(DungeonFloor dungeonFloor){
        File tilesetFolder = new File(FileManager.getTilesetsFolder(), dungeonFloor.getTileset());
        World world = Bukkit.getWorld(dungeonFloor.getWorld());
        if (world == null){
            SimpleDungeons.simpleDungeons.getLogger().severe("World not created!");
            return;
        }
        System.out.println("Pasting rooms...");
        LinkedBlockingQueue<DungeonRoom> roomsToPaste = dungeonFloor.getRoomsToPaste();
        Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, new Runnable() {
            @Override
            public void run() {
                if (roomsToPaste.isEmpty()) {
                    System.out.println("Done pasting rooms...");
                    dungeonFloor.setReady(true);
                    return;
                }

                int count = 0;
                while (count < 3){
                    count++;
                    DungeonRoom room = roomsToPaste.poll();
                    if (room == null) continue;
                    DungeonChunk chunk = room.getPasteChunk();
                    if (chunk == null) continue;
                    int level = chunk.getLevel();
                    String fileName = room.getRoomConfiguration().getFileName();
                    world.isChunkGenerated(chunk.getX(), chunk.getZ());
                    if (!world.isChunkLoaded(chunk.getX(), chunk.getZ())) {
                        world.getChunkAtAsync(chunk.getX(), chunk.getZ()).thenAccept(c ->
                                WEUtils.pasteFile(tilesetFolder, fileName, world, c.getX() * 16, ((level * 16)), c.getZ() * 16)
                        );
                    } else {
                        WEUtils.pasteFile(tilesetFolder, fileName, world, chunk.getX() * 16, ((level * 16)), chunk.getZ() * 16);
                    }

                    SignManager.findTileEntities(dungeonFloor, room);

                    FileManager.log(dungeonFloor.getRooms().indexOf(room) + "/" + (dungeonFloor.getRooms().size()-1) + " completed...");
                }

                Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, this, 20);
            }
        }, 0);
    }
}
