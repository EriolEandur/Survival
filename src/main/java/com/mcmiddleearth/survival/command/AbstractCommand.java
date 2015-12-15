/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.survival.command;

import com.mcmiddleearth.survival.utils.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur, Ivanpl
 */
public abstract class AbstractCommand {
    
    private final String[] permissionNodes;
    
    @Getter
    private final int minArgs;
    
    private boolean playerOnly = true;
    
    @Getter
    @Setter
    private String usageDescription, shortDescription;
    
    public AbstractCommand(int minArgs, boolean playerOnly, String... permissionNodes) {
        this.minArgs = minArgs;
        this.playerOnly = playerOnly;
        this.permissionNodes = permissionNodes;
    }
    
    public void handle(CommandSender cs, String... args) {
        Player p = null;
        if(cs instanceof Player) {
            p = (Player) cs;
        }
        
        if(p == null && playerOnly) {
            sendPlayerOnlyErrorMessage(cs);
            return;
        }
        
        if(p != null && !hasPermissions(p)) {
            sendNoPermsErrorMessage(p);
            return;
        }
        
        if(args.length < minArgs) {
            sendMissingArgumentErrorMessage(cs);
            return;
        }
        
        execute(cs, args);
    }
    
    protected abstract void execute(CommandSender cs, String... args);
    
    private void sendPlayerOnlyErrorMessage(CommandSender cs) {
        MessageUtil.sendErrorMessage(cs, "You have to be logged in to run this command.");
    }
    
    private void sendNoPermsErrorMessage(CommandSender cs) {
        MessageUtil.sendErrorMessage(cs, "You don't have permission to run this command.");
    }
    
    private void sendMissingArgumentErrorMessage(CommandSender cs) {
        MessageUtil.sendErrorMessage(cs, "You're missing arguments for this command.");
    }
    
    protected void sendSaveDataErrorMessage(CommandSender cs) {
        MessageUtil.sendErrorMessage(cs, "There was an error. Data not saved to disk.");
    }
    
    protected boolean hasPermissions(Player p) {
        if(permissionNodes != null) {
            for(String permission : permissionNodes) {
                if (!p.hasPermission(permission)) {
                    return false;
                }
            }
        }
        return true;
    }
    
}