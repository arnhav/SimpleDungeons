package com.tyrriel.simpledungeons.gui;

import com.tyrriel.simpledungeons.objects.DungeonConfiguration;
import com.tyrriel.simpledungeons.objects.instance.DungeonGroup;
import org.bukkit.entity.Player;

public class OpenGUI {

    private GUIView guiView;
    private Player player;
    private DungeonConfiguration dungeonConfiguration;
    private DungeonGroup dungeonGroup;

    public OpenGUI(GUIView guiView, Player player){
        setGuiView(guiView);
        setPlayer(player);
    }

    public void setGuiView(GUIView guiView) {
        this.guiView = guiView;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setDungeonConfiguration(DungeonConfiguration dungeonConfiguration) {
        this.dungeonConfiguration = dungeonConfiguration;
    }

    public void setDungeonGroup(DungeonGroup dungeonGroup) {
        this.dungeonGroup = dungeonGroup;
    }

    public GUIView getGuiView() {
        return guiView;
    }

    public Player getPlayer() {
        return player;
    }

    public DungeonConfiguration getDungeonConfiguration() {
        return dungeonConfiguration;
    }

    public DungeonGroup getDungeonGroup() {
        return dungeonGroup;
    }
}
