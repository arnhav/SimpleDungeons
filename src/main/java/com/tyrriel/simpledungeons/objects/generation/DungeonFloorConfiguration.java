package com.tyrriel.simpledungeons.objects.generation;

import java.util.List;

public class DungeonFloorConfiguration {

    private int pathLength;
    private String boss;
    private List<String> mobs;
    private List<RoomConfiguration> rooms;

    public DungeonFloorConfiguration(){}

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public void setMobs(List<String> mobs) {
        this.mobs = mobs;
    }

    public void setRooms(List<RoomConfiguration> rooms) {
        this.rooms = rooms;
    }

    public int getPathLength() {
        return pathLength;
    }

    public String getBoss() {
        return boss;
    }

    public List<String> getMobs() {
        return mobs;
    }

    public List<RoomConfiguration> getRooms() {
        return rooms;
    }
}
