package com.tyrriel.simpledungeons.objects.generation;

import com.sk89q.worldedit.util.Direction;
import com.tyrriel.simpledungeons.objects.enums.RoomType;

import java.util.HashMap;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomConfiguration that = (RoomConfiguration) o;
        return sizeX == that.sizeX &&
                sizeY == that.sizeY &&
                sizeZ == that.sizeZ &&
                fileName.equals(that.fileName) &&
                roomType == that.roomType &&
                openings.equals(that.openings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, sizeX, sizeY, sizeZ, roomType, openings);
    }
}
