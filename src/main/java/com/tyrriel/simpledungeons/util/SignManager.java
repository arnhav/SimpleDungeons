package com.tyrriel.simpledungeons.util;

import com.tyrriel.simpledungeons.objects.*;
import com.tyrriel.simpledungeons.objects.enums.TriggerType;
import com.tyrriel.simpledungeons.objects.generation.DungeonChunk;
import com.tyrriel.simpledungeons.objects.generation.DungeonRoom;
import com.tyrriel.simpledungeons.objects.mechanics.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.block.data.Rotatable;

import java.util.HashSet;

public class SignManager {

    public static void findTileEntities(Dungeon dungeon){
        System.out.println("Finding TileEntities...");
        HashSet<DungeonChunk> seen = new HashSet<>();
        for (DungeonRoom dr : dungeon.getRooms()){
            for (DungeonChunk dc : dr.getChunks()) {
                if (seen.contains(dc)) continue;
                seen.add(dc);
                World world = Bukkit.getWorld(dc.getWorld());
                if (world == null) continue;
                world.getChunkAtAsync(dc.getX(), dc.getZ()).thenAccept(chunk -> {
                    BlockState[] tileEntities = chunk.getTileEntities();
                    findAndUnpackSigns(dungeon, tileEntities);
                });
            }
        }
        System.out.println("Done finding TileEntities...");
        dungeon.setReady(true);
    }

    public static void findAndUnpackSigns(Dungeon dungeon, BlockState[] tileEntities){
        for (BlockState tileEntity : tileEntities){
            Location location = tileEntity.getLocation();
            Block block = location.getBlock();
            if (!(tileEntity instanceof Sign)) continue;
            Sign sign = (Sign) tileEntity;
            if (!sign.getLine(0).contains("[")) continue;
            String type = sign.getLine(0);

            if (type.equalsIgnoreCase("[START]")){
                dungeon.setStart(sign.getLocation());
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("[BOSS]")){
                String mob = sign.getLine(1);
                if (mob.equalsIgnoreCase("")) continue;
                DungeonBoss db = new DungeonBoss(mob, location);
                if (!addTrigger(dungeon, sign, db))
                    db.spawn();
                dungeon.setDungeonBoss(db);
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("[DOOR]")){
                String name = sign.getLine(1);
                if (name.equalsIgnoreCase("")) continue;
                String key = sign.getLine(2);
                DungeonDoor dd = new DungeonDoor(name, key, block.getWorld());
                Material mat = Material.getMaterial(sign.getLine(3));
                dd.setDoorMaterial(mat);
                dungeon.addDungeonDoor(dd, location);
            }
            if (type.equalsIgnoreCase("[PORTAL]")){
                DungeonPortal dp = new DungeonPortal(location);
                if (!addTrigger(dungeon, sign, dp))
                    dp.trigger();
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("[CHEST]")){
                Rotatable signData = (Rotatable) block.getBlockData().clone();
                BlockFace facing = signData.getRotation();
                String loottable = sign.getLine(1);
                DungeonChest dc = new DungeonChest(location, facing);
                dc.setLoottable(loottable);
                dungeon.addChest(location, dc);
                if (addTrigger(dungeon, sign, dc)) {
                    block.setType(Material.AIR);
                    continue;
                }
                dc.trigger();
            }
            if (type.equalsIgnoreCase("[MOB]")){
                String mob = sign.getLine(1);
                if (mob.equalsIgnoreCase("")) continue;
                DungeonMob dm = new DungeonMob(mob, location);
                if (!addTrigger(dungeon, sign, dm))
                    dm.spawn();
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("[BLOCK]")){
                Material init = Material.getMaterial(sign.getLine(1));
                Material triggered = Material.getMaterial(sign.getLine(2));
                if (init == null || triggered == null) continue;
                DungeonBlock db = new DungeonBlock(init, triggered, location);
                addTrigger(dungeon, sign, db);
                block.setType(init);
            }
        }
    }

    private static boolean addTrigger(Dungeon dungeon, Sign sign, Object o){
        if (sign.getLine(3).equalsIgnoreCase("")) return false;
        String line = sign.getLine(3);
        DungeonTrigger dt;
        if (line.contains(" ")){
            String thing = line.substring(line.indexOf(" ")+1);
            if (line.contains("M")){
                dt = new DungeonTrigger(TriggerType.MOB, thing);
                dungeon.addTrigger(dt, o);
            }
            if (line.contains("D")){
                dt = new DungeonTrigger(TriggerType.DOOR, thing);
                dungeon.addTrigger(dt, o);
            }
        } else {
            if (line.equalsIgnoreCase("BOSS")){
                dt = new DungeonTrigger(TriggerType.BOSS);
                dungeon.addTrigger(dt, o);
            }
        }
        return true;
    }

}
