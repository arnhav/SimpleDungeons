package com.tyrriel.simpledungeons.util;

import com.sk89q.worldedit.util.Direction;
import com.tyrriel.simpledungeons.objects.DungeonFloor;
import com.tyrriel.simpledungeons.objects.enums.RoomType;
import com.tyrriel.simpledungeons.objects.generation.*;

import java.util.ArrayList;
import java.util.List;

public class RoomConfigurationUtil {

    public static RoomConfiguration findAndSelectStartRoom(List<RoomConfiguration> rooms){
        List<RoomConfiguration> shortList = new ArrayList<>();
        for (RoomConfiguration room : rooms){
            if (room.getRoomType() == RoomType.START)
                shortList.add(room);
        }
        if (!shortList.isEmpty())
            return shortList.get(RandomUtil.randomWithRange(0, shortList.size()-1));
        return null;
    }

    public static List<DungeonChunk> getChunksForRoomConfiguration(RoomConfiguration rc, DungeonChunk nwc){
        List<DungeonChunk> chunks = new ArrayList<>();
        int Y = nwc.getLevel();
        int X = nwc.getX();
        int Z = nwc.getZ();
        for (int y = Y; y <= Y+rc.getSizeY(); y++){
            for (int x = X; x <= X+rc.getSizeX(); x++){
                for (int z = Z; z <= Z+rc.getSizeZ(); z++){
                    DungeonChunk dc = new DungeonChunk(nwc.getWorld(), x, z, y);
                    chunks.add(dc);
                }
            }
        }
        return chunks;
    }

    public static DungeonChunk getDungeonChunkForOpening(DungeonChunk pc, RoomConfigurationOpening rco){
        return new DungeonChunk(pc.getWorld(), pc.getX()+rco.getX(), pc.getZ()+rco.getZ(), pc.getLevel()+rco.getY());
    }

    public static List<RoomConfiguration> getAllRoomsOpenInDirection(DungeonFloor df, RoomConfiguration prc, Direction direction){
        List<RoomConfiguration> rooms = df.getDungeonFloorConfiguration().getRooms();
        List<RoomConfiguration> list = new ArrayList<>();
        for (RoomConfiguration rc : rooms){
            if (rc.getOpenings().size() == 1) continue;
            if (rc.getRoomType() != RoomType.GENERIC) continue;
            if (!rc.getOpenings().containsKey(direction)) continue;
            if (rc.getLimit() != -1){
                if (getNumRoomConfigsInDungeon(df, rc) >= rc.getLimit()) continue;
            }
            if (!prc.getIncompat().isEmpty()) {
                if (prc.getIncompat().contains(rc.getFileName())) continue;
            }
            list.add(rc);
        }
        return list;
    }

    public static DungeonChunk getNWBMostCorner(DungeonChunk pc, RoomConfigurationOpening rco){
        return new DungeonChunk(pc.getWorld(), pc.getX()-rco.getX(), pc.getZ()-rco.getZ(), pc.getLevel()-rco.getY());
    }

    public static List<RoomConfiguration> getBossRooms(List<RoomConfiguration> rooms){
        List<RoomConfiguration> list = new ArrayList<>();
        for (RoomConfiguration rc : rooms){
            if (rc.getRoomType() == RoomType.BOSS)
                list.add(rc);
        }
        return list;
    }

    public static List<RoomConfiguration> getEndCapRoomsInDirection(List<RoomConfiguration> rooms, Direction direction){
        List<RoomConfiguration> list = new ArrayList<>();
        for (RoomConfiguration rc : rooms){
            if (rc.getRoomType() == RoomType.GENERIC && rc.getOpenings().size() == 1 && rc.getOpenings().containsKey(direction))
                list.add(rc);
        }
        return list;
    }

    public static int getNumRoomConfigsInDungeon(DungeonFloor d, RoomConfiguration rc){
        int count = 0;
        for (DungeonRoom dr : d.getRooms()){
            if (dr.getRoomConfiguration().equals(rc)) count++;
        }
        return count;
    }

    public static boolean doesDungeonHaveRoomConfig(DungeonFloor d, RoomConfiguration rc){
        return getNumRoomConfigsInDungeon(d, rc) > 0;
    }

}
