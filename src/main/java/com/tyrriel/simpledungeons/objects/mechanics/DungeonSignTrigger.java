package com.tyrriel.simpledungeons.objects.mechanics;

import com.tyrriel.simpledungeons.objects.DungeonFloor;
import com.tyrriel.simpledungeons.objects.enums.TriggerType;

public class DungeonSignTrigger {

    private String name;
    private DungeonFloor df;

    public DungeonSignTrigger(String name, DungeonFloor df){
        setName(name);
        setDf(df);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDf(DungeonFloor df) {
        this.df = df;
    }

    public String getName() {
        return name;
    }

    public DungeonFloor getDf() {
        return df;
    }

    public void trigger(){
        df.trigger(new DungeonTrigger(TriggerType.GENERIC, name));
    }
}
