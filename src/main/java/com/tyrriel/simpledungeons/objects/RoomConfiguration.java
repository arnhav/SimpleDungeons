package com.tyrriel.simpledungeons.objects;

import com.sk89q.worldedit.util.Direction;
import com.tyrriel.simpledungeons.objects.enums.RoomType;

import java.util.HashMap;

public class RoomConfiguration {

    private String fileName;
    private int sizeX, sizeY, sizeZ;
    private RoomType roomType;
    private HashMap<Direction, RoomConfigurationOpening> openings;

    public RoomConfiguration(String fileName, int sizeX, int sizeY, int sizeZ, RoomType roomType, HashMap<Direction, RoomConfigurationOpening> openings){
        setFileName(fileName);
        setSizeX(sizeX);
        setSizeY(sizeY);
        setSizeZ(sizeZ);
        setRoomType(roomType);
        setOpenings(openings);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public void setSizeZ(int sizeZ) {
        this.sizeZ = sizeZ;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setOpenings(HashMap<Direction, RoomConfigurationOpening> openings) {
        this.openings = openings;
    }

    public String getFileName() {
        return fileName;
    }

    public int getSizeX() {
        return sizeX-1;
    }

    public int getSizeY() {
        return sizeY-1;
    }

    public int getSizeZ() {
        return sizeZ-1;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public HashMap<Direction, RoomConfigurationOpening> getOpenings() {
        return openings;
    }

    @Override
    public String toString() {
        return "RoomConfiguration{" +
                "fileName='" + fileName + '\'' +
                ", sizeX=" + sizeX +
                ", sizeY=" + sizeY +
                ", sizeZ=" + sizeZ +
                ", roomType=" + roomType +
                ", openings=" + openings +
                '}';
    }
}
