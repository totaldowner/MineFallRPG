package com.totaldowner.minefall;

import org.bukkit.plugin.Plugin;

public class MFCrafting {

    public static void resetRecipes(Plugin plugin){
        plugin.getServer().clearRecipes();
        
        
    }
}
