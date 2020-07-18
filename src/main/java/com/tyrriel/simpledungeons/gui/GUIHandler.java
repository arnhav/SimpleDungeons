package com.tyrriel.simpledungeons.gui;

import com.tyrriel.simpledungeons.objects.DungeonConfiguration;
import com.tyrriel.simpledungeons.objects.instance.DungeonGroup;
import com.tyrriel.simpledungeons.objects.instance.DungeonPlayer;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GUIHandler {

    public static HashMap<Inventory, OpenGUI> openGUIs = new HashMap<>();

    public static List<Integer> signPositions = Arrays.asList(
            11, 13, 15, 29, 31, 33
    );

    public static void openGroupSelectionGUI(Player player, DungeonConfiguration dungeonConfiguration){
        Inventory inventory = Bukkit.createInventory(null, 45, dungeonConfiguration.getName());
        fillInventory(inventory, GUIItems.getGUIItem(Material.BLACK_STAINED_GLASS_PANE, " "), 0, 45);
        List<DungeonGroup> dgs = DungeonManager.dungeonGroups.get(dungeonConfiguration);
        for (int i = 0; i < signPositions.size(); i++){
            ItemStack itemStack = GUIItems.getGUIItem(Material.OAK_SIGN, ChatColor.DARK_AQUA + "Group: " + (i+1));
            DungeonGroup dg;
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            dg = dgs.get(i);
            lore.add(ChatColor.GOLD + "Players: " + (dg.getPlayers().isEmpty() ? ChatColor.GRAY + "None" : ""));
            for (DungeonPlayer dp : dg.getPlayers()){
                Player temp = dp.getPlayer();
                lore.add(ChatColor.GRAY + " - " + ChatColor.WHITE + temp.getName());
            }
            lore.add(" ");
            if (dg.isPlayingDungeon()){
                lore.add(ChatColor.RED + "CURRENTLY PLAYING DUNGEON");
            } else {
                if (dg.getPlayers().size() == 5){
                    lore.add(ChatColor.RED + "FULL");
                } else {
                    lore.add(ChatColor.YELLOW + "[Click to Join]");
                }
            }

            itemStack = GUIItems.setLore(itemStack, lore);
            inventory.setItem(signPositions.get(i), itemStack);
        }
        player.openInventory(inventory);
        OpenGUI openGUI = new OpenGUI(GUIView.GROUP_SELECTION, player);
        openGUI.setDungeonConfiguration(dungeonConfiguration);
        openGUIs.put(inventory, openGUI);
    }

    public static void openGroupGUI(Player player, DungeonGroup dungeonGroup){
        Inventory inventory = Bukkit.createInventory(null, 45, "Ready?");
        fillInventory(inventory, GUIItems.getGUIItem(Material.BLACK_STAINED_GLASS_PANE, " "), 0, 45);
        inventory.setItem(0, GUIItems.getGUIItem(Material.BARRIER, ChatColor.RED + "<- Leave Group"));
        for (int i = 0; i < 5; i++){
            if (i >= dungeonGroup.getPlayers().size()){
                inventory.setItem(i+2, GUIItems.getGUIItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "[EMPTY]"));
                inventory.setItem((i+2) + (9*4), GUIItems.getGUIItem(Material.CYAN_TERRACOTTA, ChatColor.GRAY + " "));
            } else {
                DungeonPlayer dp = dungeonGroup.getPlayers().get(i);

                ItemStack head = GUIItems.getPlayerHead(dp.getPlayer(), (dp.isReady() ? ChatColor.GREEN : ChatColor.RED) + dp.getPlayer().getName());

                inventory.setItem(i+2, head);
                inventory.setItem((i+2) + (9*4), dp.isReady() ?
                        GUIItems.getGUIItem(Material.GREEN_TERRACOTTA, ChatColor.GREEN + "READY") :
                        GUIItems.getGUIItem(Material.RED_TERRACOTTA, ChatColor.RED + "NOT READY"));
            }
        }
        player.openInventory(inventory);
        OpenGUI openGUI = new OpenGUI(GUIView.GROUP, player);
        openGUI.setDungeonGroup(dungeonGroup);
        openGUIs.put(inventory, openGUI);
    }

    private static void fillInventory(Inventory inventory, ItemStack itemStack, int startIndex, int endIndex){
        for (int i = startIndex; i < endIndex; i++){
            inventory.setItem(i, itemStack);
        }
    }
}
