package com.tyrriel.simpledungeons.util;

import com.tyrriel.simpledungeons.objects.*;
import com.tyrriel.simpledungeons.objects.enums.PortalType;
import com.tyrriel.simpledungeons.objects.enums.TriggerType;
import com.tyrriel.simpledungeons.objects.generation.DungeonChunk;
import com.tyrriel.simpledungeons.objects.generation.DungeonRoom;
import com.tyrriel.simpledungeons.objects.mechanics.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Rotatable;

public class SignManager {

    public static void findTileEntities(DungeonFloor df, DungeonRoom dr){
        for (DungeonChunk dc : dr.getChunks()) {
            World world = Bukkit.getWorld(df.getWorld());
            if (world == null) continue;
            world.getChunkAtAsync(dc.getX(), dc.getZ()).thenAccept(c ->{
                BlockState[] tileEntities = c.getTileEntities();
                findAndUnpackSigns(df, tileEntities);
            });
        }
    }

    public static void findAndUnpackSigns(DungeonFloor df, BlockState[] tileEntities){
        for (BlockState tileEntity : tileEntities){
            Location location = tileEntity.getLocation();
            Block block = location.getBlock();
            if (!(tileEntity instanceof Sign)) continue;
            Sign sign = (Sign) tileEntity;
            if (!sign.getLine(0).contains("[")) continue;
            String type = sign.getLine(0);

            if (type.equalsIgnoreCase("[START]")){
                df.setStart(location);
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("[BOSS]")){
                block.setType(Material.AIR);
                DungeonBoss db = new DungeonBoss(df.getDungeonFloorConfiguration().getBoss(), location);
                if (!addTrigger(df, sign, db))
                    db.spawn();
                df.setDungeonBoss(db);
            }
            if (type.equalsIgnoreCase("[DOOR]")){
                String name = sign.getLine(1);
                if (name.equalsIgnoreCase("")) continue;
                String key = sign.getLine(2);
                DungeonDoor dd = new DungeonDoor(name, key, block.getWorld());
                Material mat = Material.getMaterial(sign.getLine(3));
                dd.setDoorMaterial(mat);
                df.addDungeonDoor(dd, location);
            }
            if (type.equalsIgnoreCase("[PORTAL]")){
                String portal = sign.getLine(1);
                if (portal.equalsIgnoreCase("")) continue;
                PortalType pt = PortalType.valueOf(portal);
                DungeonPortal dp = new DungeonPortal(location, pt);
                if (!addTrigger(df, sign, dp))
                    dp.trigger();
                block.setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("[TRIGGER]")){
                String name = sign.getLine(1);
                if (name.equalsIgnoreCase("")) continue;
                DungeonSignTrigger dst = new DungeonSignTrigger(name, df);
                addTrigger(df, sign, dst);
            }
            if (type.equalsIgnoreCase("[CHEST]")){
                Rotatable signData = (Rotatable) block.getBlockData().clone();
                BlockFace facing = signData.getRotation();
                String loottable = sign.getLine(1);
                DungeonChest dc = new DungeonChest(location, facing);
                dc.setLoottable(loottable);
                df.addChest(location, dc);
                if (!addTrigger(df, sign, dc)) {
                    dc.trigger();
                    continue;
                }
                block.setType(Material.AIR);
                df.addChest(location, dc);
            }
            if (type.equalsIgnoreCase("[MOB]")){
                String i = sign.getLine(1);
                block.setType(Material.AIR);
                if (i.equalsIgnoreCase("")) continue;
                if (!StringUtils.isNumeric(i)) continue;
                int index = Integer.parseInt(i);
                if (df.getDungeonFloorConfiguration().getMobs().size() <= index) continue;
                String mob = df.getDungeonFloorConfiguration().getMobs().get(index);
                DungeonMob dm = new DungeonMob(mob, location);
                if (!addTrigger(df, sign, dm))
                    dm.spawn();
            }
            if (type.equalsIgnoreCase("[BLOCK]")){
                Material init = Material.getMaterial(sign.getLine(1));
                Material triggered = Material.getMaterial(sign.getLine(2));
                if (init == null || triggered == null) continue;
                DungeonBlock db = new DungeonBlock(init, triggered, location);
                addTrigger(df, sign, db);
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
            if (line.contains("T")){
                dt = new DungeonTrigger(TriggerType.GENERIC, thing);
                dungeonFloor.addTrigger(dt, o);
            }
        } else {
            if (line.equalsIgnoreCase("BOSS")){
                dt = new DungeonTrigger(TriggerType.BOSS);
                dungeonFloor.addTrigger(dt, o);
            }
            if (line.equalsIgnoreCase("R")){
                dt = new DungeonTrigger(TriggerType.REDSTONE, sign.getLocation());
                dungeonFloor.addTrigger(dt, o);
            }
        }
        return true;
    }

}
