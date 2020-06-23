package com.tyrriel.simpledungeons.util;

import com.tyrriel.simpledungeons.objects.Dungeon;
import com.tyrriel.simpledungeons.objects.DungeonChunk;
import com.tyrriel.simpledungeons.objects.DungeonRoom;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;

import java.util.HashSet;

public class SignManager {

    static BukkitAPIHelper bukkitAPIHelper = MythicMobs.inst().getAPIHelper();

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
    }

    public static void findAndUnpackSigns(Dungeon dungeon, BlockState[] tileEntities){
        for (BlockState tileEntity : tileEntities){
            if (!(tileEntity instanceof Sign)) continue;
            Sign sign = (Sign) tileEntity;
            if (!sign.getLine(0).equalsIgnoreCase("[SD]")) continue;
            String type = sign.getLine(1);

            if (type.equalsIgnoreCase("START")){
                dungeon.setStart(sign.getLocation());
                sign.getBlock().setType(Material.AIR);
            }
            if (type.equalsIgnoreCase("MOB")){
                String mob = sign.getLine(2);
                try {
                    Entity entity = bukkitAPIHelper.spawnMythicMob(mob, sign.getLocation());
                    sign.getBlock().setType(Material.AIR);
                } catch (InvalidMobTypeException e) {
                    e.printStackTrace();
                }
            }
            if (type.equalsIgnoreCase("CHEST")){
                tileEntity.getBlock().setType(Material.TRAPPED_CHEST);
            }
            if (type.equalsIgnoreCase("BLOCK")){
                Material material = Material.getMaterial(sign.getLine(2));
                sign.getBlock().setType(material == null ? Material.AIR : material);
            }
        }
    }

}
