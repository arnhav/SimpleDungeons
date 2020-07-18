package com.tyrriel.simpledungeons.objects.instance;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public class DungeonPlayer {

    private Player player;
    private Location location;

    private boolean isReady = false;
    private boolean isDead = false;

    public DungeonPlayer(Player player){
        setPlayer(player);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isReady() {
        return isReady;
    }

    public boolean isDead() {
        return isDead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DungeonPlayer that = (DungeonPlayer) o;
        return isReady == that.isReady &&
                player.equals(that.player) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, location, isReady);
    }
}
