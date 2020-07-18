package com.tyrriel.simpledungeons.objects;

import com.tyrriel.simpledungeons.objects.instance.DungeonGroup;

public class Dungeon {

    private DungeonConfiguration dungeonConfiguration;
    private DungeonFloor dungeonFloor;
    private DungeonGroup dungeonGroup;
    private int floor;

    public Dungeon(DungeonConfiguration dungeonConfiguration, int floor) {
        setDungeonConfiguration(dungeonConfiguration);
        setFloor(floor);
    }

    public void setDungeonConfiguration(DungeonConfiguration dungeonConfiguration) {
        this.dungeonConfiguration = dungeonConfiguration;
    }

    public void setDungeonFloor(DungeonFloor dungeonFloor) {
        this.dungeonFloor = dungeonFloor;
    }

    public void setDungeonGroup(DungeonGroup dungeonGroup) {
        this.dungeonGroup = dungeonGroup;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public DungeonConfiguration getDungeonConfiguration() {
        return dungeonConfiguration;
    }

    public DungeonFloor getDungeonFloor() {
        return dungeonFloor;
    }

    public DungeonGroup getDungeonGroup() {
        return dungeonGroup;
    }

    public int getFloor() {
        return floor;
    }
}
