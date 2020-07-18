package com.tyrriel.simpledungeons.commands;

import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.gui.GUIHandler;
import com.tyrriel.simpledungeons.objects.DungeonConfiguration;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SimpleDungeonCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("")) {
            return false;
        }

        if (!sender.isOp()) return true;

        if (args.length == 0){
            sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "SimpleDungeon Help");
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GRAY + "[*] /sd menu <dungeon> <player>");
            sender.sendMessage(ChatColor.GRAY + "[*] /sd list");
            sender.sendMessage(ChatColor.GRAY + "[*] /sd item <get|add|delete|list> [item]");
        }

        if (args.length <= 0) return true;

        if (args[0].equalsIgnoreCase("list")){
            sender.sendMessage("Dungeons:");
            for (String dungeon : DungeonManager.dungeonConfigurations.keySet()){
                sender.sendMessage(" - " + dungeon);
            }
        }

        if (args.length < 2) return true;

        if (args[0].equalsIgnoreCase("menu")){
            String config = args[1];
            DungeonConfiguration dc = DungeonManager.dungeonConfigurations.get(config);
            if (dc == null) return true;
            Player player = Bukkit.getPlayerExact(args[2]);
            if (player == null) return true;
            GUIHandler.openGroupSelectionGUI(player, dc);
        }

        if (args[0].equals("item")){
            if (args[1].equalsIgnoreCase("list")){
                sender.sendMessage(ChatColor.GRAY + "-== Dungeon Items ==-");
                for (String key : DungeonManager.dungeonItems.keySet()){
                    sender.sendMessage(ChatColor.GRAY + " - " + key);
                }
            }
            if (args[1].equalsIgnoreCase("add")){
                if (args.length < 3) return true;
                String key = args[2];
                if (!(sender instanceof Player)) return true;
                if (DungeonManager.dungeonItems.containsKey(key)) return true;
                ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand();
                if (itemStack.getType() == Material.AIR) return true;
                FileManager.addItem(key, itemStack);
                DungeonManager.dungeonItems.put(key, itemStack);
                sender.sendMessage(ChatColor.YELLOW + "Item Added!");
            }
            if (args[1].equalsIgnoreCase("delete")){
                if (args.length < 3) return true;
                String key = args[2];
                FileManager.deleteItem(key);
                DungeonManager.dungeonItems.remove(key);
                sender.sendMessage(ChatColor.YELLOW + "Item Deleted!");
            }
            if (args[1].equalsIgnoreCase("get")){
                if (args.length < 3) return true;
                String key = args[2];
                if (!(sender instanceof Player)) return true;
                if (!DungeonManager.dungeonItems.containsKey(key)) return true;
                ((Player) sender).getInventory().addItem(DungeonManager.dungeonItems.get(key));
            }
        }

        return true;
    }
}
