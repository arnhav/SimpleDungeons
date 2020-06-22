package com.tyrriel.simpledungeons.objects;

import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Dungeon {

    private String name;
    private String tileset;
    private String world;
    private DungeonConfiguration dungeonConfiguration;
    private ArrayList<DungeonRoom> rooms;
    private LinkedBlockingQueue<DungeonRoom> roomsToPaste;
    private ArrayList<Chest> openedChest;
    private boolean bossDefeated = false;

    public Dungeon(String name, String tileset, String world, ArrayList<DungeonRoom> rooms, LinkedBlockingQueue<DungeonRoom> roomsToPaste, DungeonConfiguration dungeonConfiguration){
        setName(name);
        setTileset(tileset);
        setWorld(world);
        setRooms(rooms);
        setRoomsToPaste(roomsToPaste);
        setDungeonConfiguration(dungeonConfiguration);

        openedChest = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTileset(String tileset) {
        this.tileset = tileset;
    }

    public void setRooms(ArrayList<DungeonRoom> rooms) {
        this.rooms = rooms;
    }

    public void setRoomsToPaste(LinkedBlockingQueue<DungeonRoom> roomsToPaste) {
        this.roomsToPaste = roomsToPaste;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public void setDungeonConfiguration(DungeonConfiguration dungeonConfiguration) {
        this.dungeonConfiguration = dungeonConfiguration;
    }

    public void setChestToOpened(Chest chest){
        openedChest.add(chest);
    }

    public void setBossDefeated(boolean bossDefeated) {
        this.bossDefeated = bossDefeated;
    }

    public String getName() {
        return name;
    }

    public String getTileset() {
        return tileset;
    }

    public ArrayList<DungeonRoom> getRooms() {
        return rooms;
    }

    public LinkedBlockingQueue<DungeonRoom> getRoomsToPaste() {
        return roomsToPaste;
    }

    public String getWorld() {
        return world;
    }

    public DungeonConfiguration getDungeonConfiguration() {
        return dungeonConfiguration;
    }

    public ArrayList<Chest> getOpenedChest() {
        return openedChest;
    }

    public boolean isBossDefeated() {
        return bossDefeated;
    }

    public void addRoom(DungeonRoom dungeonRoom){
        rooms.add(dungeonRoom);
        roomsToPaste.add(dungeonRoom);
    }
}
