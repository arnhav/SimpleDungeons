package com.tyrriel.simpledungeons.util;

import com.sk89q.worldedit.util.Direction;
import com.tyrriel.simpledungeons.objects.*;
import com.tyrriel.simpledungeons.objects.enums.RoomType;
import com.tyrriel.simpledungeons.objects.generation.DungeonChunk;
import com.tyrriel.simpledungeons.objects.generation.DungeonRoom;
import com.tyrriel.simpledungeons.objects.generation.RoomConfiguration;
import com.tyrriel.simpledungeons.objects.generation.RoomConfigurationOpening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DungeonUtil {

    public static void generateNextRoom(Dungeon dungeon, DungeonRoom pr, int count){
        List<RoomConfiguration> rooms = dungeon.getDungeonConfiguration().getRooms();
        RoomConfiguration prc = pr.getRoomConfiguration();
        HashMap<Direction, RoomConfigurationOpening> prcos = prc.getOpenings();
        for (Direction direction : prcos.keySet()){
            RoomConfigurationOpening prco = prcos.get(direction);
            DungeonChunk popening = RoomConfigurationUtil.getDungeonChunkForOpening(pr.getPasteChunk(), prco);

            DungeonChunk nextChunk = getNextChunkInDirection(popening, direction, 1);

            Direction inverse = DirectionUtil.getInverse(direction);

            List<RoomConfiguration> potentialConfigs = RoomConfigurationUtil.getAllRoomsOpenInDirection(rooms, inverse);

            DungeonRoom room = setRoom(dungeon, potentialConfigs, nextChunk, inverse);
            if (room == null) continue;
            count++;
            if (count > dungeon.getDungeonConfiguration().getPathLength()) return;
            generateNextRoom(dungeon, room, count);
        }
    }

    public static void generateBossRoom(Dungeon dungeon){
        List<RoomConfiguration> bossRoomConfigs = RoomConfigurationUtil.getBossRooms(dungeon.getDungeonConfiguration().getRooms());
        HashMap<DungeonChunk, Direction> endCaps = getEndCapsWithDirection(dungeon);
        endCaps.putAll(getAllEndCaps(dungeon));
        for (RoomConfiguration brc : bossRoomConfigs) {
            if (doesDungeonHaveRoomType(dungeon, RoomType.BOSS)) continue;
            for (DungeonChunk ec : endCaps.keySet()) {
                Direction direction = endCaps.get(ec);
                if (!brc.getOpenings().containsKey(direction)) continue;
                DungeonChunk nwb = RoomConfigurationUtil.getNWBMostCorner(ec, brc.getOpenings().get(direction));
                if (nwb.getLevel() < 0) continue;
                List<DungeonChunk> chunks = RoomConfigurationUtil.getChunksForRoomConfiguration(brc, nwb);
                if (areAnyChunksAlreadyRooms(dungeon, chunks)) continue;

                DungeonRoom room = new DungeonRoom(chunks, brc);
                dungeon.addRoom(room);
                return;
            }
        }
    }

    public static void generateEndCaps(Dungeon dungeon){
        HashMap<DungeonChunk, Direction> endCaps = getEndCapsWithDirection(dungeon);
        for (DungeonChunk ec : endCaps.keySet()) {
            Direction direction = endCaps.get(ec);
            List<RoomConfiguration> ecConfigs = RoomConfigurationUtil.getEndCapRoomsInDirection(dungeon.getDungeonConfiguration().getRooms(), direction);
            setRoom(dungeon, ecConfigs, ec, direction);
        }
    }

    public static DungeonChunk getNextChunkInDirection(DungeonChunk chunk, Direction direction, int chunks){
        int x = chunk.getX();
        int z = chunk.getZ();
        int l = chunk.getLevel();
        if (direction == Direction.NORTH){
            z-=chunks;
        }
        if (direction == Direction.SOUTH){
            z+=chunks;
        }
        if (direction == Direction.EAST){
            x+=chunks;
        }
        if (direction == Direction.WEST) {
            x-=chunks;
        }
        return new DungeonChunk(chunk.getWorld(), x, z, l);
    }

    public static DungeonRoom getDungeonRoom(Dungeon dungeon, DungeonChunk chunk){
        for (DungeonRoom dr : dungeon.getRooms()){
            for (DungeonChunk dc : dr.getChunks()){
                if (dc.getX() == chunk.getX()
                        && dc.getZ() == chunk.getZ()
                        && dc.getLevel() == chunk.getLevel())
                    return dr;
            }
        }
        return null;
    }

    public static boolean isAlreadyRoom(Dungeon dungeon, DungeonChunk chunk){
        return getDungeonRoom(dungeon, chunk) != null;
    }

    public static boolean areAnyChunksAlreadyRooms(Dungeon dungeon, List<DungeonChunk> chunks){
        for (DungeonChunk chunk : chunks){
            if (isAlreadyRoom(dungeon, chunk)) return true;
        }
        return false;
    }

    public static boolean doesDungeonHaveRoomType(Dungeon dungeon, RoomType rt){
        for (DungeonRoom dr: dungeon.getRooms()){
            RoomConfiguration rc = dr.getRoomConfiguration();
            if (rc.getRoomType() == rt) return true;
        }
        return false;
    }

    public static DungeonRoom setRoom(Dungeon dungeon, List<RoomConfiguration> potentialConfigs, DungeonChunk nextChunk, Direction direction){
        List<RoomConfiguration> shortList = new ArrayList<>();
        for (RoomConfiguration porc : potentialConfigs){
            DungeonChunk ponwb = RoomConfigurationUtil.getNWBMostCorner(nextChunk, porc.getOpenings().get(direction));
            if (ponwb.getLevel() < 0) continue;
            List<DungeonChunk> pochunks = RoomConfigurationUtil.getChunksForRoomConfiguration(porc, ponwb);
            if (areAnyChunksAlreadyRooms(dungeon, pochunks)) continue;
            shortList.add(porc);
        }
        if (shortList.isEmpty()) return null;

        RoomConfiguration rc = shortList.get(RandomUtil.randomWithRange(0, shortList.size()-1));
        if (rc == null) return null;
        HashMap<Direction, RoomConfigurationOpening> rcos = rc.getOpenings();
        RoomConfigurationOpening rco = rcos.get(direction);
        DungeonChunk nwb = RoomConfigurationUtil.getNWBMostCorner(nextChunk, rco);
        List<DungeonChunk> chunks = RoomConfigurationUtil.getChunksForRoomConfiguration(rc, nwb);
        DungeonRoom room = new DungeonRoom(chunks, rc);
        dungeon.addRoom(room);
        return room;
    }

    public static HashMap<DungeonChunk, Direction> getEndCapsWithDirection(Dungeon dungeon){
        HashMap<DungeonChunk, Direction> map = new HashMap<>();
        for (DungeonRoom dr: dungeon.getRooms()){
            RoomConfiguration rc = dr.getRoomConfiguration();
            HashMap<Direction, RoomConfigurationOpening> rcos = rc.getOpenings();
            for (Direction direction : rcos.keySet()){
                RoomConfigurationOpening rco = rcos.get(direction);
                DungeonChunk rchunk = RoomConfigurationUtil.getDungeonChunkForOpening(dr.getPasteChunk(), rco);
                DungeonChunk checkChunk = getNextChunkInDirection(rchunk, direction, 1);
                if (isAlreadyRoom(dungeon, checkChunk)) continue;
                map.put(checkChunk, DirectionUtil.getInverse(direction));
            }
        }
        return map;
    }

    public static HashMap<DungeonChunk, Direction> getAllEndCaps(Dungeon dungeon){
        HashMap<DungeonChunk, Direction> map = new HashMap<>();
        for (DungeonRoom dr: dungeon.getRooms()){
            RoomConfiguration rc = dr.getRoomConfiguration();
            HashMap<Direction, RoomConfigurationOpening> rcos = rc.getOpenings();
            if (rcos.size() > 1) continue;
            for (Direction direction : rcos.keySet()){
                RoomConfigurationOpening rco = rcos.get(direction);
                DungeonChunk rchunk = RoomConfigurationUtil.getDungeonChunkForOpening(dr.getPasteChunk(), rco);
                map.put(rchunk, DirectionUtil.getInverse(direction));
            }
        }
        return map;
    }
}
