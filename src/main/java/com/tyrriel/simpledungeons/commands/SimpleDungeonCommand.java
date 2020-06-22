package com.tyrriel.simpledungeons.commands;

import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.data.FileManager;
import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.util.DungeonGenerator;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Bukkit;
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

        if (!sender.isOp()) return false;

        if (args.length == 0 || args.length == 1){
            sender.sendMessage("SimpleDungeon Help");
            sender.sendMessage("/sd create <tileset> <worldname>");
            sender.sendMessage("/sd delete <worldname>");
            sender.sendMessage("/sd enter <worldname>");
            sender.sendMessage("/sd leave");
        }

        if (args.length < 2) return false;

        if (args[0].equalsIgnoreCase("create")){
            if (args.length != 3) return false;
            String tileSet = args[1];
            String worldName = args[2];
            if (!isTileSetPresent(tileSet)) return false;
            if (DungeonManager.dungeons.containsKey(worldName)) return false;
            DungeonGenerator.createWorld(worldName);
            Bukkit.getScheduler().runTaskAsynchronously(SimpleDungeons.simpleDungeons, ()->{
                DungeonGenerator.createDungeon(tileSet, worldName);
            });
            Bukkit.getScheduler().runTaskLater(SimpleDungeons.simpleDungeons, ()->{
                Dungeon dungeon = DungeonManager.dungeons.get(worldName);
                if (dungeon == null) return;
                DungeonGenerator.placeRooms(dungeon);
                sender.sendMessage("Dungeon created!");
            }, 5*20);
        }

        if (args[0].equalsIgnoreCase("delete")){
            if (!DungeonManager.dungeons.containsKey(args[1])) return false;
            World world = Bukkit.getWorld(DungeonManager.dungeons.get(args[1]).getWorld());
            File worldFile = new File(Bukkit.getWorldContainer(), args[1]);
            Bukkit.unloadWorld(world, false);
            DungeonManager.dungeons.remove(args[1]);
            sender.sendMessage("Dungeon " + args[1] + " deleted.");
            FileManager.deleteDirectory(worldFile);
        }

        if (args[0].equalsIgnoreCase("enter")){
            if (!(sender instanceof Player)) return false;
            Player p = (Player) sender;
            if (DungeonManager.playersInDungeon.containsKey(p)) return false;
            Dungeon dungeon = DungeonManager.dungeons.get(args[1]);
            World world = Bukkit.getWorld(dungeon.getWorld());
            if (world == null) return false;
            if (!dungeon.getRoomsToPaste().isEmpty()) return false;
            DungeonManager.playersInDungeon.put(p, args[1]);
            DungeonManager.playerLocations.put(p, p.getLocation());
            Location location = new Location(world, 7, 4, 6);
            p.teleport(location);
        }

        if (args[0].equalsIgnoreCase("leave")){
            if (!(sender instanceof Player)) return false;
            if (!DungeonManager.playersInDungeon.containsKey(sender)) return false;
            Location location = DungeonManager.playerLocations.get(sender);
            ((Player) sender).teleport(location);
            DungeonManager.playersInDungeon.remove(sender);
            DungeonManager.playerLocations.remove(sender);
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
