package com.tyrriel.simpledungeons.data;

import com.sk89q.worldedit.util.Direction;
import com.tyrriel.simpledungeons.SimpleDungeons;
import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.objects.DungeonConfiguration;
import com.tyrriel.simpledungeons.objects.generation.*;
import com.tyrriel.simpledungeons.objects.enums.RoomType;
import com.tyrriel.simpledungeons.objects.instance.DungeonGroup;
import com.tyrriel.simpledungeons.util.DungeonManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileManager {

    public static FileConfiguration config;
    public static FileConfiguration data;

    private static File itemsFile;
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
                loadConfig();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createItemsFile(JavaPlugin plugin){
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            itemsFile = new File(plugin.getDataFolder(), "items.yml");
            data = new YamlConfiguration();
            if (!itemsFile.exists()) {
                plugin.getLogger().info("items.yml not found, creating!");
                data.set("Items.test", new ItemStack(Material.DIAMOND));
                save(data, itemsFile);

            } else {
                plugin.getLogger().info("items.yml found, loading!");
            }
            loadItems(itemsFile);
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
            pw.println("-=== SimpleDungeons Log " + format.format(date) + " ===-");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getTilesetsFolder(){
        return new File(SimpleDungeons.simpleDungeons.getDataFolder(), "tilesets");
    }

    private static void loadConfig(){
        config = SimpleDungeons.simpleDungeons.getConfig();
        ConfigurationSection configSection = config.getConfigurationSection("Dungeons");
        if (configSection == null) return;
        for (String path : configSection.getKeys(false)){
            String name = configSection.getString(path + ".Name");
            boolean infinite = configSection.getBoolean(path + ".Infinite");
            List<String> bosses = configSection.getStringList(path + ".Bosses");
            List<List<String>> mobSets = new ArrayList<>();
            ConfigurationSection mobsSection = config.getConfigurationSection("Dungeons." + path + ".MobSets");
            if (mobsSection != null) {
                for (String mobPath : mobsSection.getKeys(false)) {
                    List<String> mobs = mobsSection.getStringList(mobPath);
                    mobSets.add(mobs);
                }
            }
            HashMap<String, List<String>> lootTables = new HashMap<>();
            ConfigurationSection lootSection = config.getConfigurationSection("Dungeons." + path + ".LootTables");
            if (lootSection != null){
                for (String lootPath : lootSection.getKeys(false)){
                    List<String> loot = lootSection.getStringList(lootPath);
                    lootTables.put(lootPath, loot);
                }
            }
            List<String> tilesets = configSection.getStringList(path + ".TileSets");
            DungeonConfiguration dc = new DungeonConfiguration(path, name, infinite, bosses, mobSets, lootTables, tilesets);
            DungeonManager.dungeonConfigurations.put(path, dc);

            List<DungeonGroup> dgs = new ArrayList<>();
            for (int i = 0; i < 6; i++){
                Dungeon dungeon = new Dungeon(dc, 0);
                DungeonGroup dg = new DungeonGroup(dungeon);
                dgs.add(dg);
                dungeon.setDungeonGroup(dg);
                DungeonManager.dungeons.add(dungeon);
            }
            DungeonManager.dungeonGroups.put(dc, dgs);
        }
    }

    private static void loadItems(File file){
        data = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection cs = data.getConfigurationSection("Items");
        if (cs == null) return;
        for (String path : cs.getKeys(false)){
            ItemStack itemStack = cs.getItemStack(path);
            DungeonManager.dungeonItems.put(path, itemStack);
        }
    }

    public static DungeonFloorConfiguration readTilesetConfig(File folder){
        DungeonFloorConfiguration dungeonFloorConfiguration = new DungeonFloorConfiguration();
        File file = new File(folder, "config.yml");
        if (!file.exists()) return null;
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);

        if (fileConfiguration.isSet("PathLength")) {
            int pathLength = fileConfiguration.getInt("PathLength");
            dungeonFloorConfiguration.setPathLength(pathLength);
        } else {
            fileConfiguration.set("PathLength", 20);
            save(fileConfiguration, file);
            dungeonFloorConfiguration.setPathLength(20);
        }

        ConfigurationSection roomSection = fileConfiguration.getConfigurationSection("Rooms");
        List<RoomConfiguration> rooms = new ArrayList<>();
        if (roomSection != null) {
            for (String path : roomSection.getKeys(false)) {
                String schematic = path;
                RoomType roomType = RoomType.valueOf(roomSection.getString(path + ".RoomType"));
                int limit = roomSection.getInt(path + ".Limit");
                List<String> incompat = roomSection.getStringList(path + ".Incompat");
                int sx = roomSection.getInt(path + ".Size.x");
                int sy = roomSection.getInt(path + ".Size.y");
                int sz = roomSection.getInt(path + ".Size.z");

                HashMap<Direction, RoomConfigurationOpening> openings = new HashMap<>();
                ConfigurationSection openingsSection = fileConfiguration.getConfigurationSection("Rooms." + path + ".Openings");
                if (openingsSection == null) continue;
                for (String dir : openingsSection.getKeys(false)){
                    try {
                        Direction direction = Direction.valueOf(dir);
                        int x = openingsSection.getInt(dir + ".x");
                        int y = openingsSection.getInt(dir + ".y");
                        int z = openingsSection.getInt(dir + ".z");

                        RoomConfigurationOpening opening = new RoomConfigurationOpening(x, y, z, direction);
                        openings.put(direction, opening);
                    } catch (IllegalArgumentException e){
                        SimpleDungeons.simpleDungeons.getLogger().severe(dir + " is not a valid direction!");
                        return null;
                    }
                }

                if (limit <= 0)
                    limit = -1;

                RoomConfiguration roomConfiguration = new RoomConfiguration(schematic, roomType, limit, incompat, sx, sy, sz, openings);
                rooms.add(roomConfiguration);
            }
        }
        dungeonFloorConfiguration.setRooms(rooms);
        return dungeonFloorConfiguration;
    }

    public static void addItem(String key, ItemStack itemStack){
        data = YamlConfiguration.loadConfiguration(itemsFile);
        data.set("Items." + key, itemStack);
        save(data, itemsFile);
    }

    public static void deleteItem(String key){
        data = YamlConfiguration.loadConfiguration(itemsFile);
        data.set("Items." + key, null);
        save(data, itemsFile);
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

    private static void save(FileConfiguration fileConfiguration, File file){
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
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


}
