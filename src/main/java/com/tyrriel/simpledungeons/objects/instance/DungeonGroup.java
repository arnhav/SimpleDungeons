package com.tyrriel.simpledungeons.objects.instance;

import com.tyrriel.simpledungeons.objects.Dungeon;

import java.util.ArrayList;
import java.util.List;

public class DungeonGroup {

    private List<DungeonPlayer> players;

    private Dungeon dungeon;

    private boolean isPlayingDungeon = false;

    public DungeonGroup(Dungeon dungeon){
        setDungeon(dungeon);
        players = new ArrayList<>();
    }

    public void addPlayer(DungeonPlayer dp){
        players.add(dp);
    }

    public void removePlayer(DungeonPlayer dp){
        players.remove(dp);
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void setPlayingDungeon(boolean playingDungeon) {
        isPlayingDungeon = playingDungeon;
    }

    public List<DungeonPlayer> getPlayers() {
        return players;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public boolean isPlayingDungeon() {
        return isPlayingDungeon;
    }
}
