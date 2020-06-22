package com.tyrriel.simpledungeons.data;

import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.objects.DungeonConfiguration;
import com.tyrriel.simpledungeons.objects.enums.RoomConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileManager {

    public static FileConfiguration config;

    private static File log;

    public static void createConfig(JavaPlugin plugin){
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            File file = new File(plugin.getDataFolder(), "config.yml");
            config = new YamlConfiguration();
            if (!file.exists()) {
                plugin.getLogger().info("config.yml not found, creating!");
                plugin.saveDefaultConfig();
            } else {
                plugin.getLogger().info("config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createLogFile(JavaPlugin plugin) {
        File logsFolder = new File(plugin.getDataFolder(), "logs");
        if (!logsFolder.exists()){
            logsFolder.mkdirs();
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        log = new File(logsFolder, format.format(date) + ".txt");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(log, true);
            PrintWriter pw = new PrintWriter(fileWriter);
            pw.println("-===SimpleRegeneration Log " + format.format(date) + "===-");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getTilesetsFolder(){
        return new File(SimpleDungeons.simpleDungeons.getDataFolder(), "tilesets");
    }

    public static DungeonConfiguration readTilesetConfig(File folder){
        DungeonConfiguration dungeonConfiguration = new DungeonConfiguration();
        HashMap<RoomConfiguration, List<String>> map = new HashMap<>();
        File file = new File(folder, "config.yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (file.exists()){
            ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("Rooms");
            if (configurationSection != null) {
                for (String path : configurationSection.getKeys(false)) {
                    try {
                        RoomConfiguration roomConfiguration = RoomConfiguration.valueOf(path);
                        map.put(roomConfiguration, configurationSection.getStringList(path));
                    } catch (IllegalArgumentException e){
                        SimpleDungeons.simpleDungeons.getLogger().severe(path + " is not a recognized room configuration!");
                        return null;
                    }
                }
                dungeonConfiguration.setRoomNames(map);
            } else {
                for (RoomConfiguration roomConfiguration : RoomConfiguration.values()){
                    fileConfiguration.set("Rooms." + roomConfiguration.toString(), Collections.singletonList("REPLACEME"));
                }
                try {
                    fileConfiguration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileConfiguration.isSet("PathLength")) {
                int pathLength = fileConfiguration.getInt("PathLength");
                dungeonConfiguration.setPathLength(pathLength);
            } else {
                fileConfiguration.set("PathLength", 20);
                try {
                    fileConfiguration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dungeonConfiguration.setPathLength(20);
            }
        } else {
            return null;
        }
        return dungeonConfiguration;
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static void log(String message){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(log, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(PrintWriter pw = new PrintWriter(fileWriter) ) {
            pw.println("[" + format.format(date) + "] " + message);
        }
    }

}
