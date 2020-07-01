package com.tyrriel.simpledungeons.commands;

import com.tyrriel.simpledungeons.gui.GUIHandler;
import com.tyrriel.simpledungeons.objects.DungeonConfiguration;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            sender.sendMessage(ChatColor.GRAY + "[*] /sd menu <dungeon> <player>");
            sender.sendMessage(ChatColor.GRAY + "[*] /sd list");
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

        return true;
    }
}
