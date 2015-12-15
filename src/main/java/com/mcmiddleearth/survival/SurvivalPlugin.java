/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.survival;

import com.mcmiddleearth.survival.command.SurvivalCommandExecutor;
import com.mcmiddleearth.survival.data.PluginData;
import java.io.IOException;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Eriol_Eandur
 */
public class SurvivalPlugin extends JavaPlugin{
 
    @Getter
    private static JavaPlugin pluginInstance;
    
    @Override
    public void onEnable() {
        pluginInstance = this;
        try {
            PluginData.loadData();
        } catch (IOException ex) {}
        getCommand("survival").setExecutor(new SurvivalCommandExecutor());
        getLogger().info("Enabled!");
    }
}
