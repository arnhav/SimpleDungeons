package com.tyrriel.simpledungeons.objects;

import java.util.List;

public class DungeonConfiguration {

    private String name, id;
    private List<String> tilesets;
    private int floor;

    public DungeonConfiguration(String id, String name, List<String> tilesets){
        setId(id);
        setName(name);
        setTilesets(tilesets);
        setFloor(0);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTilesets(List<String> tilesets) {
        this.tilesets = tilesets;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getTilesets() {
        return tilesets;
    }

    public int getFloor() {
        return floor;
    }
}
