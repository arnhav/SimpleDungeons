package com.tyrriel.simpledungeons.objects;

import java.util.List;

public class DungeonRoom {

    private RoomConfiguration roomConfiguration;
    private List<DungeonChunk> chunks;

    public DungeonRoom(List<DungeonChunk> chunks, RoomConfiguration roomConfiguration){
        setChunks(chunks);
        setRoomConfiguration(roomConfiguration);
    }

    public void setRoomConfiguration(RoomConfiguration roomConfiguration) {
        this.roomConfiguration = roomConfiguration;
    }

    public void setChunks(List<DungeonChunk> chunks) {
        this.chunks = chunks;
    }

    public RoomConfiguration getRoomConfiguration() {
        return roomConfiguration;
    }

    public List<DungeonChunk> getChunks() {
        return chunks;
    }

    public DungeonChunk getPasteChunk(){
        if (chunks.isEmpty()) return null;
        DungeonChunk pc = chunks.get(0);
        for (DungeonChunk dc : chunks){
            if (dc.getX() <= pc.getX() && dc.getZ() <= pc.getZ() && dc.getLevel() <= pc.getLevel())
                pc = dc;
        }
        return pc;
    }

    @Override
    public String toString() {
        return "DungeonRoom{" +
                "roomConfiguration=" + roomConfiguration +
                ", chunks=" + chunks +
                '}';
    }
}
