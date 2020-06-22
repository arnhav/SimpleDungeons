package com.tyrriel.simpledungeons.objects;

import com.tyrriel.simpledungeons.objects.enums.RoomConfiguration;

import java.util.HashMap;
import java.util.List;

public class DungeonConfiguration {

    private HashMap<RoomConfiguration, List<String>> roomNames;
    private int pathLength;

    public void setRoomNames(HashMap<RoomConfiguration, List<String>> roomNames) {
        this.roomNames = roomNames;
    }

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }

    public HashMap<RoomConfiguration, List<String>> getRoomNames() {
        return roomNames;
    }

    public int getPathLength() {
        return pathLength;
    }
}
