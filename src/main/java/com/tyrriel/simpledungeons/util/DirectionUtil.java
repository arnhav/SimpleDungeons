package com.tyrriel.simpledungeons.util;

import com.sk89q.worldedit.util.Direction;
import com.tyrriel.simpledungeons.objects.enums.RoomConfiguration;

public class DirectionUtil {
    public static boolean doesRoomGoUp(RoomConfiguration roomConfiguration){
        return roomConfiguration.toString().contains("U");
    }

    public static boolean doesRoomGoDown(RoomConfiguration roomConfiguration){
        return roomConfiguration.toString().contains("D");
    }

    public static boolean doesRoomGoUpInDirection(Direction direction, RoomConfiguration roomConfiguration){
        if (direction == Direction.NORTH && roomConfiguration.toString().contains("UN"))
            return true;
        if (direction == Direction.SOUTH && roomConfiguration.toString().contains("US"))
            return true;
        if (direction == Direction.EAST && roomConfiguration.toString().contains("UE"))
            return true;
        if (direction == Direction.WEST && roomConfiguration.toString().contains("UW"))
            return true;
        return false;
    }

    public static boolean doesRoomGoDownInDirection(Direction direction, RoomConfiguration roomConfiguration){
        if (direction == Direction.NORTH && roomConfiguration.toString().contains("DN"))
            return true;
        if (direction == Direction.SOUTH && roomConfiguration.toString().contains("DS"))
            return true;
        if (direction == Direction.EAST && roomConfiguration.toString().contains("DE"))
            return true;
        if (direction == Direction.WEST && roomConfiguration.toString().contains("DW"))
            return true;
        return false;
    }

    public static boolean isRoomOpenInDirection(Direction direction, RoomConfiguration roomConfiguration){
        if (direction == Direction.NORTH && roomConfiguration.toString().contains("N"))
            return true;
        if (direction == Direction.SOUTH && roomConfiguration.toString().contains("S"))
            return true;
        if (direction == Direction.EAST && roomConfiguration.toString().contains("E"))
            return true;
        if (direction == Direction.WEST && roomConfiguration.toString().contains("W"))
            return true;
        return false;
    }

    public static boolean canRoomConfigConnectToDirection(Direction direction, RoomConfiguration roomConfiguration, int level){
        if (roomConfiguration.toString().contains("BOSS") || roomConfiguration.toString().contains("START"))
            return false;
        if (doesRoomGoDown(roomConfiguration))
            return doesRoomGoDownInDirection(direction, roomConfiguration) && level > 0;
        if (doesRoomGoUp(roomConfiguration))
            return doesRoomGoUpInDirection(direction, roomConfiguration);
        if (direction == Direction.NORTH && roomConfiguration.toString().contains("S"))
            return true;
        if (direction == Direction.SOUTH && roomConfiguration.toString().contains("N"))
            return true;
        if (direction == Direction.EAST && roomConfiguration.toString().contains("W"))
            return true;
        if (direction == Direction.WEST && roomConfiguration.toString().contains("E"))
            return true;
        return false;
    }

    public static Direction getFacing(RoomConfiguration roomConfiguration){
        if (roomConfiguration.toString().contains("N"))
            return Direction.SOUTH;
        if (roomConfiguration.toString().contains("S"))
            return Direction.NORTH;
        if (roomConfiguration.toString().contains("E"))
            return Direction.WEST;
        if (roomConfiguration.toString().contains("W"))
            return Direction.EAST;
        return Direction.NORTH;
    }

    public static Direction getInverse(Direction direction){
        if (direction == Direction.NORTH)
            return Direction.SOUTH;
        if (direction == Direction.SOUTH)
            return Direction.NORTH;
        if (direction == Direction.EAST)
            return Direction.WEST;
        if (direction == Direction.WEST)
            return Direction.EAST;
        return Direction.NORTH;
    }
}
