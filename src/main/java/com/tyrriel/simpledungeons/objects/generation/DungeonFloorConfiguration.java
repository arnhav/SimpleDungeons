package com.tyrriel.simpledungeons.objects.generation;

import java.util.List;

public class DungeonFloorConfiguration {

    private int pathLength;
    private List<RoomConfiguration> rooms;

    public DungeonFloorConfiguration(){}

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }

    public void setRooms(List<RoomConfiguration> rooms) {
        this.rooms = rooms;
    }

    public int getPathLength() {
        return pathLength;
    }

    public List<RoomConfiguration> getRooms() {
        return rooms;
    }
}
