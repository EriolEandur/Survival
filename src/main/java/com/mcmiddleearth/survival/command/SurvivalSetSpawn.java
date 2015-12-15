/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.survival.command;

import com.mcmiddleearth.survival.data.PluginData;
import com.mcmiddleearth.survival.utils.MessageUtil;
import java.io.IOException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
class SurvivalSetSpawn extends AbstractCommand {

    public SurvivalSetSpawn(String permissionNodes) {
        super(1,true,permissionNodes);
    }

    @Override
    protected void execute(CommandSender cs, String... args) {
        try {
            if(args[0].equalsIgnoreCase("pvp")) {
                PluginData.setPvpSpawnLocation(((Player)cs).getLocation());
                sendPvpSetMessage(cs);
                PluginData.saveData();
            }
            else if(args[0].equalsIgnoreCase("survival")) {
                PluginData.setSurvivalSpawnLocation(((Player)cs).getLocation());
                sendSurvivalSetMessage(cs);
                PluginData.saveData();
            }
            else {
                sendWrongArgumentMessage(cs);
            }
        }
        catch(IOException e) {
            sendSaveDataErrorMessage(cs);
        }
    }

    private void sendPvpSetMessage(CommandSender cs) {
        MessageUtil.sendInfoMessage(cs, "Teleport location for /survival leave was set to your current position.");
    }

    private void sendSurvivalSetMessage(CommandSender cs) {
        MessageUtil.sendInfoMessage(cs, "Default teleport location for /survival join was set to your current position.");
    }

    private void sendWrongArgumentMessage(CommandSender cs) {
        MessageUtil.sendErrorMessage(cs, "Illegal argument for this command ('survival' and 'pvp' only)."); 
    }
}