package com.tyrriel.simpledungeons.objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DungeonPlayer {

    private Player player;
    private Location location;
    private String dungeon;

    public DungeonPlayer(Player player, Location location, String dungeon){
        setPlayer(player);
        setLocation(location);
        setDungeon(dungeon);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setDungeon(String dungeon) {
        this.dungeon = dungeon;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public String getDungeon() {
        return dungeon;
    }
}
