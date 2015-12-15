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
class SurvivalLeave extends AbstractCommand {

    public SurvivalLeave(String permissionNodes) {
        super(0,true,permissionNodes);
    }

    @Override
    protected void execute(CommandSender cs, String... args) {
        if(!PluginData.isInSurvival((Player) cs)) {
            sendNotInSurvivalErrorMessage(cs);
        }
        else {
            sendLeaveSurvivalMessage(cs);
            try {
                PluginData.leaveSurvival((Player) cs);
                PluginData.saveData();
            } catch (IOException ex) {
                sendSaveDataErrorMessage(cs);
            }
        }
    }

    private void sendNotInSurvivalErrorMessage(CommandSender cs) {
        MessageUtil.sendErrorMessage(cs, "You are not at the survival map.");
    }

    private void sendLeaveSurvivalMessage(CommandSender cs) {
        MessageUtil.sendInfoMessage(cs, "You left the survival map.");
    }
    
}
