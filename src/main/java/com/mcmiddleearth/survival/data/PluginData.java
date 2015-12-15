/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.survival.data;

import com.mcmiddleearth.survival.SurvivalPlugin;
import com.mcmiddleearth.survival.utils.BukkitUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class PluginData {
    
    private static final Map<OfflinePlayer,Location> joinLocations = new LinkedHashMap<>();
    
    private static final List<OfflinePlayer> survivalPlayers = new ArrayList<>();
    
    @Setter
    private static Location pvpSpawnLocation = new Location(Bukkit.getWorlds().get(0),0,64,0);
    
    @Setter
    private static Location survivalSpawnLocation = new Location(Bukkit.getWorlds().get(0),0,64,0);
    
    private static final File locationFile = new File(SurvivalPlugin.getPluginInstance().getDataFolder()
                                                   + File.separator + "SuvivalLocations.suv");
    
    private static final File playerFile = new File(SurvivalPlugin.getPluginInstance().getDataFolder()
                                                   + File.separator + "SuvivalPlayers.suv");
    
    public static boolean isInSurvival(OfflinePlayer player) {
        for(OfflinePlayer search : survivalPlayers) {
            if(BukkitUtil.isSame(search, player)) {
                return true;
            }
        }
        return false;
    }
    
    public static void joinSurvival(Player player){
        survivalPlayers.add(player);
        Location joinLoc = getJoinLocation(player);
        if(joinLoc!=null) {
            player.teleport(getJoinLocation(player));
        }
        else {
            player.teleport(survivalSpawnLocation);
        }
    }
    
    public static void leaveSurvival(Player player){
        saveJoinLocation(player);
        List<OfflinePlayer> removeList = new ArrayList<>();
        for(OfflinePlayer search : survivalPlayers) {
            if(BukkitUtil.isSame(search, player)) {
                removeList.add(search);
            }
        }
        survivalPlayers.removeAll(removeList);
        player.teleport(pvpSpawnLocation);
    }
    
    private static void saveJoinLocation(Player player) {
        for(OfflinePlayer search: joinLocations.keySet()) {
            if(BukkitUtil.isSame(search, player)) {
                joinLocations.remove(search);
            }
        }
        joinLocations.put(player, player.getLocation());
    }
    
    private static Location getJoinLocation(OfflinePlayer player) {
        for(OfflinePlayer search: joinLocations.keySet()) {
            if(BukkitUtil.isSame(search, player)) {
                return joinLocations.get(search);
            }
        }
        return null;
    }
    
    public static void saveData() throws IOException {
        try {
            playerFile.createNewFile();
            locationFile.createNewFile();
            FileWriter fw = new FileWriter(playerFile.toString());
            try (PrintWriter writer = new PrintWriter(fw)) {
                for(OfflinePlayer player : survivalPlayers) {
                    writer.println(player.getUniqueId().toString());
                }
            }
            fw = new FileWriter(locationFile.toString());
            try (PrintWriter writer = new PrintWriter(fw)) {
                writer.println(pvpSpawnLocation.getWorld().getName());
                writer.println(pvpSpawnLocation.getBlockX()+" "
                             + pvpSpawnLocation.getBlockY()+" "
                             + pvpSpawnLocation.getBlockZ()+" ");
                writer.println(survivalSpawnLocation.getWorld().getName());
                writer.println(survivalSpawnLocation.getBlockX()+" "
                             + survivalSpawnLocation.getBlockY()+" "
                             + survivalSpawnLocation.getBlockZ()+" ");
                for(OfflinePlayer player : joinLocations.keySet()) {
                    Location loc = joinLocations.get(player);
                    writer.println(loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ()+" "+player.getUniqueId().toString());
                }
            }
        }
        catch (IOException e) {
            SurvivalPlugin.getPluginInstance().getLogger().log(Level.WARNING, "Can't write survival data to disk!");
            throw e;
        }
    }
    
    public static void loadData() throws IOException {
        try {
            try (Scanner scanner = new Scanner(playerFile)) {
                while(scanner.hasNext()) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(scanner.nextLine()));
                    survivalPlayers.add(player);
                }
            }
            try (Scanner scanner = new Scanner(locationFile)) {
                World pvpWorld = Bukkit.getWorld(scanner.nextLine());
                if(pvpWorld == null) {
                    pvpWorld = Bukkit.getWorlds().get(0);
                }
                pvpSpawnLocation = new Location(pvpWorld, scanner.nextInt(),
                        scanner.nextInt(),
                        scanner.nextInt());
                scanner.nextLine();
                World survivalWorld = Bukkit.getWorld(scanner.nextLine());
                if(survivalWorld == null) {
                    survivalWorld = Bukkit.getWorlds().get(0);
                }
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int z = scanner.nextInt();
                scanner.nextLine();
                survivalSpawnLocation = new Location(survivalWorld, x,y,z);
                while(scanner.hasNext()) {
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    z = scanner.nextInt();
                    Location loc = new Location(survivalSpawnLocation.getWorld(),x,y,z);
                    String uuidString = scanner.nextLine();
                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuidString.trim()));
                    joinLocations.put(player, loc);
                }
            }
        }
        catch (IOException e) {
            if(e instanceof FileNotFoundException) {
                SurvivalPlugin.getPluginInstance().getLogger().log(Level.WARNING, "Can't find survival data files on disk!");
            }
            else {
                SurvivalPlugin.getPluginInstance().getLogger().log(Level.WARNING, "Can't read survival data to disk!");
            }
            throw e;
        }
    }
}
