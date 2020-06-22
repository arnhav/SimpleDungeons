package com.tyrriel.simpledungeons.util;

import com.sk89q.worldedit.util.Direction;
import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.objects.*;
import com.tyrriel.simpledungeons.objects.enums.RoomConfiguration;
import org.bukkit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DungeonUtil {

    public static void goInDirection(Dungeon dungeon, Direction direction, String world, int prevx, int prevz, int pathLength, int count, int level, File tilesetFolder){
        RoomConfiguration roomConfiguration = pickRoomConfig();
        while (!DirectionUtil.canRoomConfigConnectToDirection(direction, roomConfiguration, level)){
            roomConfiguration = pickRoomConfig();
        }

        DungeonChunk chunk = getNextChunkInDirection(new DungeonChunk(world, prevx, prevz), direction);
        addRoom(dungeon, chunk, roomConfiguration, direction, tilesetFolder, level, pathLength, count);
    }

    private static void addRoom(Dungeon dungeon, DungeonChunk chunk, RoomConfiguration roomConfiguration, Direction direction, File tilesetFolder, int level, int pathLength, int count){
        if (chunk == null) return;
        if (isAlreadyRoom(dungeon, chunk, level)) return;

        String fileName = getRoomName(roomConfiguration);

        DungeonRoom room = new DungeonRoom(chunk, level, roomConfiguration, fileName);

        if (DirectionUtil.doesRoomGoDownInDirection(direction, roomConfiguration)){
            DungeonRoom roomBelow = new DungeonRoom(chunk, level-1, roomConfiguration, fileName);
            if (isAlreadyRoom(dungeon, chunk, level-1)) return;
            dungeon.addRoom(roomBelow);
            dungeon.addRoomToPaste(roomBelow);
            level--;
        }

        if (DirectionUtil.doesRoomGoUpInDirection(direction, roomConfiguration)){
            DungeonRoom roomAbove = new DungeonRoom(chunk, level+1, roomConfiguration, fileName);
            if (isAlreadyRoom(dungeon, chunk, level+1)) return;
            dungeon.addRoom(roomAbove);
            level++;
        }

        dungeon.addRoom(room);
        dungeon.addRoomToPaste(room);
        FileManager.log(room.toString());

        if (DirectionUtil.isRoomOpenInDirection(Direction.NORTH, roomConfiguration)) {
            count = continueIn(dungeon, Direction.NORTH, RoomConfiguration._S__, chunk, pathLength, count, level, tilesetFolder);
        }
        if (DirectionUtil.isRoomOpenInDirection(Direction.SOUTH, roomConfiguration)) {
            count = continueIn(dungeon, Direction.SOUTH, RoomConfiguration.N___, chunk, pathLength, count, level, tilesetFolder);
        }
        if (DirectionUtil.isRoomOpenInDirection(Direction.EAST, roomConfiguration)) {
            count = continueIn(dungeon, Direction.EAST, RoomConfiguration.___W, chunk, pathLength, count, level, tilesetFolder);
        }
        if (DirectionUtil.isRoomOpenInDirection(Direction.WEST, roomConfiguration)) {
            continueIn(dungeon, Direction.WEST, RoomConfiguration.__E_, chunk, pathLength, count, level, tilesetFolder);
        }
    }

    private static int continueIn(Dungeon dungeon, Direction direction, RoomConfiguration roomConfiguration, DungeonChunk chunk, int pathLength, int count, int level, File tilesetFolder){
        if (count < pathLength) {
            count+=1;
            goInDirection(dungeon, direction, chunk.getWorld(), chunk.getX(), chunk.getZ(), pathLength, count, level, tilesetFolder);
            return count;
        } else {
            String fileName = getRoomName(roomConfiguration);
            DungeonChunk newChunk = getNextChunkInDirection(chunk, direction);
            if (isAlreadyRoom(dungeon, newChunk, level)) return count;
            DungeonRoom room = new DungeonRoom(newChunk, level, roomConfiguration, fileName);
            dungeon.addRoom(room);
            dungeon.addRoomToPaste(room);
            FileManager.log(room + " | ENDCAP");
            return count;
        }
    }

    public static DungeonChunk getNextChunkInDirection(DungeonChunk chunk, Direction direction){
        int x = chunk.getX();
        int z = chunk.getZ();

        if (direction == Direction.NORTH){
            z--;
        }
        if (direction == Direction.SOUTH){
            z++;
        }
        if (direction == Direction.EAST){
            x++;
        }
        if (direction == Direction.WEST) {
            x--;
        }
        return new DungeonChunk(chunk.getWorld(), x, z);
    }

    public static void pasteFile(File folder, String fileName, World world, int x, int y, int z){
        File file = new File(folder, fileName + ".schem");
        if (file.exists()){
            WEUtils.loadAndPasteSchem(file, world, x, y, z);
        } else {
            System.out.println(fileName + " not found!");
        }
    }

    public static RoomConfiguration pickRoomConfig(){
        int rand = (int) (Math.random() * ((RoomConfiguration.values().length - 1) + 1));
        return RoomConfiguration.values()[rand];
    }

    public static String getRoomName(RoomConfiguration roomConfiguration){
        List<String> rooms = DungeonGenerator.roomNames.get(roomConfiguration);
        int rand = (int) (Math.random() * ((rooms.size() - 1) + 1));
        return rooms.get(rand);
    }

    public static DungeonRoom getDungeonRoom(Dungeon dungeon, DungeonChunk chunk, int level){
        for (DungeonRoom dr : dungeon.getRooms()){
            if (dr.getChunk().getX() == chunk.getX()
                    && dr.getChunk().getZ() == chunk.getZ()
                    && dr.getLevel() == level)
                return dr;
        }
        return null;
    }

    public static boolean isAlreadyRoom(Dungeon dungeon, DungeonChunk chunk, int level){
        return getDungeonRoom(dungeon, chunk, level) != null;
    }

    public static void addBossRooms(Dungeon dungeon, DungeonRoom dungeonRoom){
        DungeonChunk chunk = dungeonRoom.getChunk();
        int level = dungeonRoom.getLevel();
        Direction direction = DirectionUtil.getFacing(dungeonRoom.getRoomConfiguration());
        DungeonChunk centerChunk = getNextChunkInDirection(chunk, direction);

        for (int l = level; l < level+2; l++){
            for (int x = centerChunk.getX() - 1; x < centerChunk.getX() + 1; x++){
                for (int z = centerChunk.getZ() - 1; z < centerChunk.getZ() + 1; z++){
                    DungeonChunk temp = new DungeonChunk(chunk.getWorld(), x, z);
                    DungeonRoom room = new DungeonRoom(temp, l, RoomConfiguration.valueOf("BOSS_" + direction.toString()), "");
                    dungeon.addRoom(room);
                }
            }
        }
    }

    public static DungeonChunk getBossPasteChunk(DungeonChunk oldChunk, Direction direction){
        String world = oldChunk.getWorld();
        int x = oldChunk.getX();
        int z = oldChunk.getZ();
        if (direction == Direction.NORTH) {
            x-=1;
            z-=2;
        }
        if (direction == Direction.SOUTH) {
            x-=1;
        }
        if (direction == Direction.EAST) {
            //x+=3;
            z-=1;
        }
        if (direction == Direction.WEST) {
            x-=2;
            z-=1;
        }
        return new DungeonChunk(world, x, z);
    }

    public static ArrayList<DungeonRoom> getEndCaps(Dungeon dungeon){
        ArrayList<DungeonRoom> list = new ArrayList<>();
        for (DungeonRoom dungeonRoom : dungeon.getRooms()){
            if (isEndCap(dungeonRoom.getRoomConfiguration())){
                if (isValidBossEndCap(dungeon, dungeonRoom))
                    list.add(dungeonRoom);
            }
        }
        return list;
    }

    public static boolean isEndCap(RoomConfiguration roomConfiguration){
        return roomConfiguration == RoomConfiguration.N___ || roomConfiguration == RoomConfiguration._S__ ||
                roomConfiguration == RoomConfiguration.__E_ || roomConfiguration == RoomConfiguration.___W;
    }

    public static boolean isValidBossEndCap(Dungeon dungeon, DungeonRoom dungeonRoom){
        DungeonChunk chunk = dungeonRoom.getChunk();
        int level = dungeonRoom.getLevel();
        Direction direction = DirectionUtil.getFacing(dungeonRoom.getRoomConfiguration());
        DungeonChunk centerChunk = getNextChunkInDirection(chunk, direction);
        if (isAlreadyRoom(dungeon, centerChunk, level)) return false;
        for (int l = level; l <= level + 1; l++){
            for (int x = centerChunk.getX() - 1; x <= centerChunk.getX() + 1; x++){
                for (int z = centerChunk.getZ() - 1; z <= centerChunk.getZ() + 1; z++){
                    DungeonChunk testChunk = new DungeonChunk(chunk.getWorld(), x, z);
                    if (!(testChunk.equals(chunk) || testChunk.equals(centerChunk)) && isAlreadyRoom(dungeon, testChunk, l)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
