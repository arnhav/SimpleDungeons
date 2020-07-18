package com.tyrriel.simpledungeons.objects;

import java.util.HashMap;
import java.util.List;

public class DungeonConfiguration {

    private String name, id;
    private boolean infinite;
    private List<String> bosses;
    private List<List<String>> mobSets;
    private HashMap<String, List<String>> lootTables;
    private List<String> tileSets;

    public DungeonConfiguration(String id, String name, boolean infinite, List<String> bosses, List<List<String>> mobSets, HashMap<String, List<String>> lootTables, List<String> tileSets){
        setId(id);
        setName(name);
        setInfinite(infinite);
        setBosses(bosses);
        setMobSets(mobSets);
        setLootTables(lootTables);
        setTileSets(tileSets);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public void setBosses(List<String> bosses) {
        this.bosses = bosses;
    }

    public void setMobSets(List<List<String>> mobSets) {
        this.mobSets = mobSets;
    }

    public void setLootTables(HashMap<String, List<String>> lootTables) {
        this.lootTables = lootTables;
    }

    public void setTileSets(List<String> tileSets) {
        this.tileSets = tileSets;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public List<String> getBosses() {
        return bosses;
    }

    public List<List<String>> getMobSets() {
        return mobSets;
    }

    public HashMap<String, List<String>> getLootTables() {
        return lootTables;
    }

    public List<String> getTileSets() {
        return tileSets;
    }
}
