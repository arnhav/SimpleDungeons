package com.tyrriel.simpledungeons.objects;

import org.bukkit.Location;
import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Dungeon {

    private String name;
    private String tileset;
    private String world;
    private DungeonConfiguration dungeonConfiguration;
    private Location start;
    private DungeonBoss dungeonBoss;

    private ArrayList<DungeonRoom> rooms;
    private LinkedBlockingQueue<DungeonRoom> roomsToPaste;
    private HashSet<Chest> openedChest;

    private HashMap<String, DungeonDoor> dungeonDoors;
    private HashMap<DungeonTrigger, List<Object>> triggeredObjects;

    public Dungeon(String name, String tileset, String world, DungeonConfiguration dungeonConfiguration){
        setName(name);
        setTileset(tileset);
        setWorld(world);
        setDungeonConfiguration(dungeonConfiguration);

        rooms = new ArrayList<>();
        roomsToPaste = new LinkedBlockingQueue<>();
        openedChest = new HashSet<>();
        triggeredObjects = new HashMap<>();
        dungeonDoors = new HashMap<>();
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

    public void setChestToOpened(Chest chest) {
        openedChest.add(chest);
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setDungeonBoss(DungeonBoss dungeonBoss) {
        this.dungeonBoss = dungeonBoss;
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

    public HashSet<Chest> getOpenedChest() {
        return openedChest;
    }

    public Location getStart() {
        return start;
    }

    public DungeonBoss getDungeonBoss() {
        return dungeonBoss;
    }

    public void addRoom(DungeonRoom dungeonRoom){
        rooms.add(dungeonRoom);
        roomsToPaste.add(dungeonRoom);
    }

    public void removeRoom(DungeonRoom dungeonRoom){
        rooms.remove(dungeonRoom);
        roomsToPaste.remove(dungeonRoom);
    }

    public void addTrigger(DungeonTrigger dungeonTrigger, Object object){
        List<Object> objects = triggeredObjects.getOrDefault(dungeonTrigger, new ArrayList<>());
        objects.add(object);
        triggeredObjects.put(dungeonTrigger, objects);
    }

    public void addDungeonDoor(DungeonDoor dd, Location point){
        String name = dd.getName();
        DungeonDoor door = dungeonDoors.get(name);
        if (door == null){
            door = dd;
            door.setPoint1(point);
        } else {
            if (door.getPoint2() == null) {
                door.setPoint2(point);
                door.fillDoor();
            }
        }
        dungeonDoors.put(name, door);
    }

    public DungeonDoor getDungeonDoor(Location location){
        for (DungeonDoor door : dungeonDoors.values()){
            Location point1 = door.getPoint1();
            Location point2 = door.getPoint2();
            if (point1 == null) continue;
            if (point2 == null) continue;
            int minX = Math.min(point1.getBlockX(), point2.getBlockX());
            int maxX = Math.max(point1.getBlockX(), point2.getBlockX());
            int minZ = Math.min(point1.getBlockZ(), point2.getBlockZ());
            int maxZ = Math.max(point1.getBlockZ(), point2.getBlockZ());
            int minY = Math.min(point1.getBlockY(), point2.getBlockY());
            int maxY = Math.max(point1.getBlockY(), point2.getBlockY());
            int X = location.getBlockX();
            int Y = location.getBlockY();
            int Z = location.getBlockZ();
            if ((minX <= X && X <= maxX) &&
                    (minY <= Y && Y <= maxY) &&
                    (minZ <= Z && Z <= maxZ))
                return door;
        }
        return null;
    }

    public void trigger(DungeonTrigger dungeonTrigger){
        List<Object> objects = triggeredObjects.get(dungeonTrigger);
        if (objects ==  null) return;
        for (Object object : objects){
            if (object.getClass() == DungeonMob.class){
                ((DungeonMob) object).spawn();
            }
            if (object.getClass() == DungeonBoss.class){
                ((DungeonBoss) object).spawn();
            }
            if (object.getClass() == DungeonBlock.class){
                ((DungeonBlock) object).trigger();
            }
        }
    }
}
