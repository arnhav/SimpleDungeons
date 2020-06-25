package com.tyrriel.simpledungeons.objects;

import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import org.bukkit.Location;

import static com.tyrriel.simpledungeons.SimpleDungeons.bukkitAPIHelper;

public class DungeonMob {

    private String type;
    private Location location;
    private boolean isSpawned = false;

    public DungeonMob(String type, Location location){
        setType(type);
        setLocation(location);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isSpawned(){
        return isSpawned;
    }

    public void spawn(){
        try {
            bukkitAPIHelper.spawnMythicMob(type, location);
            isSpawned = true;
        } catch (InvalidMobTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "DungeonMob{" +
                "type='" + type + '\'' +
                ", location=" + location +
                ", isSpawned=" + isSpawned +
                '}';
    }
}
