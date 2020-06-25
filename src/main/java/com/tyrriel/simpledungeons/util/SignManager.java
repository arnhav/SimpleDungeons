package com.tyrriel.simpledungeons.util;

import com.tyrriel.simpledungeons.objects.*;
import com.tyrriel.simpledungeons.objects.enums.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;

import java.util.HashSet;

public class SignManager {

    public static void findTileEntities(Dungeon dungeon){
        System.out.println("Finding TileEntities...");
        HashSet<DungeonChunk> seen = new HashSet<>();
        for (DungeonRoom dr : dungeon.getRooms()){
            DungeonChunk dc = dr.getChunk();
            if (seen.contains(dc)) continue;
            seen.add(dc);
            World world = Bukkit.getWorld(dc.getWorld());
            if (world == null) continue;
            world.getChunkAtAsync(dc.getX(), dc.getZ()).thenAccept(chunk -> {
                BlockState[] tileEntities = chunk.getTileEntities();
                findAndUnpackSigns(dungeon, tileEntities);
            });
        }
        System.out.println("Done finding TileEntities...");
    }

    public static void findAndUnpackSigns(Dungeon dungeon, BlockState[] tileEntities){
        for (BlockState tileEntity : tileEntities){
            Location location = tileEntity.getLocation();
            Block block = location.getBlock();
            if (!(tileEntity instanceof Sign)) continue;
            Sign sign = (Sign) tileEntity;
            if (!sign.getLine(0).equalsIgnoreCase("[SD]")) continue;
            String type = sign.getLine(1);

            if (type.equalsIgnoreCase("START")){
                dungeon.setStart(sign.getLocation());
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("BOSS")){
                String mob = sign.getLine(2);
                if (mob.equalsIgnoreCase("")) continue;
                DungeonBoss db = new DungeonBoss(mob, location);
                if (!addTrigger(dungeon, sign, db))
                    db.spawn();
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("CHEST")){
                Rotatable signData = (Rotatable) block.getBlockData().clone();
                BlockFace facing = signData.getRotation();
                block.setType(Material.TRAPPED_CHEST);
                Directional chestData = (Directional) block.getBlockData();
                chestData.setFacing(facing);
                block.setBlockData(chestData);
            }
            if (type.equalsIgnoreCase("DOOR")){
                String name = sign.getLine(2);
                if (name.equalsIgnoreCase("")) continue;
                DungeonDoor dd = new DungeonDoor(name);
                Material mat = Material.getMaterial(sign.getLine(3));
                if (mat == null) continue;
                dd.setDoorMaterial(mat);
                dungeon.addDungeonDoor(dd, location);
            }
            if (type.equalsIgnoreCase("MOB")){
                String mob = sign.getLine(2);
                if (mob.equalsIgnoreCase("")) continue;
                DungeonMob dm = new DungeonMob(mob, location);
                if (!addTrigger(dungeon, sign, dm))
                    dm.spawn();
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("BLOCK")){
                int index = sign.getLine(2).indexOf("|");
                Material init = Material.getMaterial(sign.getLine(2).substring(0, index));
                Material triggered = Material.getMaterial(sign.getLine(2).substring(index+1));
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
