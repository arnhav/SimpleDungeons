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

    public static void findTileEntities(DungeonFloor dungeonFloor){
        System.out.println("Finding TileEntities...");
        HashSet<DungeonChunk> seen = new HashSet<>();
        for (DungeonRoom dr : dungeonFloor.getRooms()){
            for (DungeonChunk dc : dr.getChunks()) {
                if (seen.contains(dc)) continue;
                seen.add(dc);
                World world = Bukkit.getWorld(dc.getWorld());
                if (world == null) continue;
                world.getChunkAtAsync(dc.getX(), dc.getZ()).thenAccept(chunk -> {
                    BlockState[] tileEntities = chunk.getTileEntities();
                    findAndUnpackSigns(dungeonFloor, tileEntities);
                });
            }
        }
        System.out.println("Done finding TileEntities...");
        dungeonFloor.setReady(true);
    }

    public static void findAndUnpackSigns(DungeonFloor dungeonFloor, BlockState[] tileEntities){
        for (BlockState tileEntity : tileEntities){
            Location location = tileEntity.getLocation();
            Block block = location.getBlock();
            if (!(tileEntity instanceof Sign)) continue;
            Sign sign = (Sign) tileEntity;
            if (!sign.getLine(0).contains("[")) continue;
            String type = sign.getLine(0);

            if (type.equalsIgnoreCase("[START]")){
                dungeonFloor.setStart(sign.getLocation());
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("[BOSS]")){
                String mob = sign.getLine(1);
                if (mob.equalsIgnoreCase("")) continue;
                DungeonBoss db = new DungeonBoss(mob, location);
                if (!addTrigger(dungeonFloor, sign, db))
                    db.spawn();
                dungeonFloor.setDungeonBoss(db);
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("[DOOR]")){
                String name = sign.getLine(1);
                if (name.equalsIgnoreCase("")) continue;
                String key = sign.getLine(2);
                DungeonDoor dd = new DungeonDoor(name, key, block.getWorld());
                Material mat = Material.getMaterial(sign.getLine(3));
                dd.setDoorMaterial(mat);
                dungeonFloor.addDungeonDoor(dd, location);
            }
            if (type.equalsIgnoreCase("[PORTAL]")){
                DungeonPortal dp = new DungeonPortal(location);
                if (!addTrigger(dungeonFloor, sign, dp))
                    dp.trigger();
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("[CHEST]")){
                Rotatable signData = (Rotatable) block.getBlockData().clone();
                BlockFace facing = signData.getRotation();
                String loottable = sign.getLine(1);
                DungeonChest dc = new DungeonChest(location, facing);
                dc.setLoottable(loottable);
                dungeonFloor.addChest(location, dc);
                if (addTrigger(dungeonFloor, sign, dc)) {
                    block.setType(Material.AIR);
                    continue;
                }
                dc.trigger();
            }
            if (type.equalsIgnoreCase("[MOB]")){
                String mob = sign.getLine(1);
                if (mob.equalsIgnoreCase("")) continue;
                DungeonMob dm = new DungeonMob(mob, location);
                if (!addTrigger(dungeonFloor, sign, dm))
                    dm.spawn();
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("[BLOCK]")){
                Material init = Material.getMaterial(sign.getLine(1));
                Material triggered = Material.getMaterial(sign.getLine(2));
                if (init == null || triggered == null) continue;
                DungeonBlock db = new DungeonBlock(init, triggered, location);
                addTrigger(dungeonFloor, sign, db);
                block.setType(init);
            }
        }
    }

    private static boolean addTrigger(DungeonFloor dungeonFloor, Sign sign, Object o){
        if (sign.getLine(3).equalsIgnoreCase("")) return false;
        String line = sign.getLine(3);
        DungeonTrigger dt;
        if (line.contains(" ")){
            String thing = line.substring(line.indexOf(" ")+1);
            if (line.contains("M")){
                dt = new DungeonTrigger(TriggerType.MOB, thing);
                dungeonFloor.addTrigger(dt, o);
            }
            if (line.contains("D")){
                dt = new DungeonTrigger(TriggerType.DOOR, thing);
                dungeonFloor.addTrigger(dt, o);
            }
        } else {
            if (line.equalsIgnoreCase("BOSS")){
                dt = new DungeonTrigger(TriggerType.BOSS);
                dungeonFloor.addTrigger(dt, o);
            }
        }
        return true;
    }

}
