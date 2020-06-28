package com.tyrriel.simpledungeons.objects.mechanics;

import com.tyrriel.simpledungeons.objects.enums.TriggerType;

import java.util.Objects;

public class DungeonTrigger {

    private TriggerType triggerType;
    private String name;

    public DungeonTrigger(TriggerType triggerType){
        setTriggerType(triggerType);
        setName("");
    }

    public DungeonTrigger(TriggerType triggerType, String name){
        setTriggerType(triggerType);
        setName(name);
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DungeonTrigger{" +
                "triggerType=" + triggerType +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DungeonTrigger that = (DungeonTrigger) o;
        return triggerType == that.triggerType &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triggerType, name);
    }
}
