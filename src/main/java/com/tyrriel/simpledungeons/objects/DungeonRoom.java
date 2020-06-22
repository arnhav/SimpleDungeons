package com.tyrriel.simpledungeons.objects;

import com.tyrriel.simpledungeons.objects.enums.RoomConfiguration;

public class DungeonRoom {

    private DungeonChunk chunk;
    private int level;
    private RoomConfiguration roomConfiguration;
    private String roomName;

    public DungeonRoom(DungeonChunk chunk, int level, RoomConfiguration roomConfiguration, String roomName){
        setChunk(chunk);
        setLevel(level);
        setRoomConfiguration(roomConfiguration);
        setRoomName(roomName);
    }

    public void setChunk(DungeonChunk chunk) {
        this.chunk = chunk;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setRoomConfiguration(RoomConfiguration roomConfiguration) {
        this.roomConfiguration = roomConfiguration;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public DungeonChunk getChunk() {
        return chunk;
    }

    public int getLevel() {
        return level;
    }

    public RoomConfiguration getRoomConfiguration() {
        return roomConfiguration;
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public String toString() {
        return "DungeonRoom{" +
                "chunk=" + chunk +
                ", level=" + level +
                ", roomConfiguration=" + roomConfiguration +
                ", roomName='" + roomName + '\'' +
                '}';
    }
}
