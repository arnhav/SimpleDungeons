package com.tyrriel.simpledungeons.objects.mechanics;

import com.tyrriel.simpledungeons.objects.enums.TriggerType;
import org.bukkit.Location;

import java.util.Objects;

public class DungeonTrigger {

    private TriggerType triggerType;
    private String name;
    private Location location;

    public DungeonTrigger(TriggerType triggerType){
        setTriggerType(triggerType);
        setName("");
    }

    public DungeonTrigger(TriggerType triggerType, String name){
        setTriggerType(triggerType);
        setName(name);
    }

    public DungeonTrigger(TriggerType triggerType, Location location){
        setTriggerType(triggerType);
        setLocation(location);
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "DungeonTrigger{" +
                "triggerType=" + triggerType +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DungeonTrigger that = (DungeonTrigger) o;
        return triggerType == that.triggerType &&
                Objects.equals(name, that.name) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triggerType, name, location);
    }
}
