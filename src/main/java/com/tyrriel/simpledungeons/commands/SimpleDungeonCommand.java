package com.tyrriel.simpledungeons.commands;

import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.util.DungeonGenerator;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class SimpleDungeonCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("")) {
            return false;
        }

        if (!sender.isOp()) return true;

        if (args.length == 0){
            sender.sendMessage("SimpleDungeon Help");
            sender.sendMessage("/sd create <tileset> <worldname>");
            sender.sendMessage("/sd delete <worldname>");
            sender.sendMessage("/sd enter <worldname>");
            sender.sendMessage("/sd leave");
        }

        if (args[0].equalsIgnoreCase("leave")){
            if (!(sender instanceof Player)) return true;
            if (!DungeonManager.playersInDungeon.containsKey(sender)) return true;
            Location location = DungeonManager.playerLocations.get(sender);
            ((Player) sender).teleport(location);
            DungeonManager.playersInDungeon.remove(sender);
            DungeonManager.playerLocations.remove(sender);
        }

        if (args.length < 2) return true;

        if (args[0].equalsIgnoreCase("create")){
            if (args.length != 3) return true;
            String tileSet = args[1];
            String worldName = args[2];
            if (!isTileSetPresent(tileSet)) return true;
            if (DungeonManager.dungeons.containsKey(worldName)) return true;
            DungeonGenerator.createWorld(worldName);
            Bukkit.getScheduler().runTaskAsynchronously(SimpleDungeons.simpleDungeons, ()->{
                DungeonGenerator.createDungeon(tileSet, worldName);
            });
            Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, ()->{
                Dungeon dungeon = DungeonManager.dungeons.get(worldName);
                if (dungeon == null) return;
                DungeonGenerator.placeRooms(dungeon);
            }, 5*20);
        }

        if (args[0].equalsIgnoreCase("delete")){
            if (!DungeonManager.dungeons.containsKey(args[1])) return true;
            World world = Bukkit.getWorld(DungeonManager.dungeons.get(args[1]).getWorld());
            File worldFile = new File(Bukkit.getWorldContainer(), args[1]);
            Bukkit.unloadWorld(world, false);
            DungeonManager.dungeons.remove(args[1]);
            sender.sendMessage("Dungeon " + args[1] + " deleted.");
            FileManager.deleteDirectory(worldFile);
        }

        if (args[0].equalsIgnoreCase("enter")){
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;
            if (DungeonManager.playersInDungeon.containsKey(p)) return true;
            Dungeon dungeon = DungeonManager.dungeons.get(args[1]);
            if (dungeon == null) {
                sender.sendMessage(ChatColor.RED + "Not a valid dungeon");
                return true;
            }
            World world = Bukkit.getWorld(dungeon.getWorld());
            if (world == null) return true;
            if (!dungeon.getRoomsToPaste().isEmpty()) return true;
            DungeonManager.playersInDungeon.put(p, args[1]);
            DungeonManager.playerLocations.put(p, p.getLocation());
            Location location = dungeon.getStart();
            if (location == null){
                sender.sendMessage(ChatColor.RED + "No start location set for dungeon.");
                return true;
            }
            p.teleport(location);
            Bukkit.dispatchCommand(p, "paper fixlight");
        }

        return true;
    }

    private boolean isTileSetPresent(String tileset){
        File[] files = FileManager.getTilesetsFolder().listFiles();
        for (File file : files){
            if (file.getName().equalsIgnoreCase(tileset))
                return true;
        }
        return false;
    }
}
