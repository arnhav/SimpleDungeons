package com.tyrriel.simpledungeons.listeners;

import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.gui.GUIHandler;
import com.tyrriel.simpledungeons.gui.GUIView;
import com.tyrriel.simpledungeons.gui.OpenGUI;
import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.objects.DungeonConfiguration;
import com.tyrriel.simpledungeons.objects.DungeonFloor;
import com.tyrriel.simpledungeons.objects.instance.DungeonGroup;
import com.tyrriel.simpledungeons.objects.instance.DungeonPlayer;
import com.tyrriel.simpledungeons.util.DungeonInstanceUtil;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory top = event.getInventory();
        Inventory clicked = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack is = event.getCurrentItem();

        if (clicked == null) return;
        if (!clicked.equals(top)) return;
        if (!GUIHandler.openGUIs.containsKey(top)) return;

        event.setCancelled(true);

        if (is == null) return;

        OpenGUI openGUI = GUIHandler.openGUIs.get(top);
        GUIView guiView = openGUI.getGuiView();

        if (guiView == GUIView.GROUP_SELECTION){
            DungeonConfiguration dc = openGUI.getDungeonConfiguration();

            List<DungeonGroup> groups = DungeonManager.dungeonGroups.get(dc);

            int slot = event.getSlot();
            int index = GUIHandler.signPositions.indexOf(slot);
            DungeonGroup dg = groups.get(index);

            DungeonPlayer dp = new DungeonPlayer(player);
            dp.setLocation(player.getLocation());
            if (dg.isPlayingDungeon()) return;
            if (dg.getPlayers().size() == 5) return;
            dg.addPlayer(dp);
            DungeonManager.dungeonPlayers.put(player, dp);

            groups.add(dg);
            DungeonManager.dungeonGroups.put(dc, groups);

            for (DungeonPlayer t : dg.getPlayers()){
                t.setReady(false);
                Player gp = t.getPlayer();
                GUIHandler.openGUIs.remove(gp.getOpenInventory().getTopInventory());
                GUIHandler.openGroupGUI(gp, dg);
            }
            updateGroupSelectionGUIs();
        }

        if (guiView == GUIView.GROUP){
            DungeonGroup dg = openGUI.getDungeonGroup();
            DungeonPlayer dp = DungeonManager.dungeonPlayers.get(player);

            if (dp == null) return;

            int slot = event.getSlot();

            if (slot == 0) {
                dg.removePlayer(dp);
                GUIHandler.openGUIs.remove(player.getOpenInventory().getTopInventory());
                GUIHandler.openGroupSelectionGUI(player, dg.getDungeon().getDungeonConfiguration());
                for (DungeonPlayer t : dg.getPlayers()){
                    Player gp = t.getPlayer();
                    GUIHandler.openGUIs.remove(gp.getOpenInventory().getTopInventory());
                    GUIHandler.openGroupGUI(gp, dg);
                }
                updateGroupSelectionGUIs();
                return;
            }

            int index = dg.getPlayers().indexOf(dp);

            if (slot != (index+2)+(9*4)) return;

            dp.setReady(is.getType() != Material.GREEN_TERRACOTTA);


            if (DungeonManager.areAllPlayersReady(dg)) {
                Dungeon dungeon = dg.getDungeon();
                DungeonFloor df = dungeon.getDungeonFloor();
                if (df.getStart() == null) {
                    DungeonInstanceUtil.sendAllPlayersInGroupMessage(dg, ChatColor.RED + "Error, start location not found");
                    return;
                }
                dg.setPlayingDungeon(true);
                for (DungeonPlayer t : dg.getPlayers()){
                    Player gp = t.getPlayer();
                    GUIHandler.openGUIs.remove(gp.getOpenInventory().getTopInventory());
                    gp.closeInventory();
                    if (df.isReady()) {
                        gp.setGameMode(GameMode.ADVENTURE);
                        gp.teleport(df.getStart());
                    }
                }
            } else {
                for (DungeonPlayer t : dg.getPlayers()){
                    Player gp = t.getPlayer();
                    GUIHandler.openGUIs.remove(gp.getOpenInventory().getTopInventory());
                    GUIHandler.openGroupGUI(gp, dg);
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Inventory top = event.getInventory();
        if (!GUIHandler.openGUIs.containsKey(top)) return;
        OpenGUI openGUI = GUIHandler.openGUIs.get(top);
        GUIHandler.openGUIs.remove(top);
        GUIView guiView = openGUI.getGuiView();
        if (guiView != GUIView.GROUP) return;
        DungeonGroup dg = openGUI.getDungeonGroup();
        Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, ()->
                GUIHandler.openGroupGUI((Player) event.getPlayer(), dg)
                ,1);
    }

    private void updateGroupSelectionGUIs(){
        List<OpenGUI> guis = new ArrayList<>(GUIHandler.openGUIs.values());
        for (OpenGUI openGUI : guis){
            if (openGUI.getGuiView() == GUIView.GROUP_SELECTION){
                DungeonConfiguration dc = openGUI.getDungeonConfiguration();
                GUIHandler.openGroupSelectionGUI(openGUI.getPlayer(), dc);
            }
        }
    }

}
