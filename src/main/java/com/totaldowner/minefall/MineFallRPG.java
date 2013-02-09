package com.totaldowner.minefall;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.totaldowner.minefall.command.DebugCommand;
import com.totaldowner.minefall.command.MFOpCommand;
import com.totaldowner.minefall.command.MineFallCommand;

/**
 * Main class of the Mine Quest RPG plugin
 * 
 * @author Timothy Swartz
 * 
 */
public class MineFallRPG extends JavaPlugin {

    @Override
    public void onEnable() {
        // enable when released
        // this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new MFEventListener(this), this);

        getCommand("mfdebug").setExecutor(new DebugCommand(this));
        getCommand("minefall").setExecutor(new MineFallCommand(this));
        getCommand("mf").setExecutor(new MineFallCommand(this));
        getCommand("mfop").setExecutor(new MFOpCommand(this));

        // Clear and add recipes
        this.getServer().clearRecipes();
        
        
        // load chunk data for already loaded chunks
        List<World> worlds = this.getServer().getWorlds();
        for (World w : worlds) {
            Chunk[] chunks = w.getLoadedChunks();
            for (Chunk c : chunks) {
                MFPersistence.loadChunk(c, this);
            }
        }
    }

    @Override
    public void onDisable() {
        // save players
        Player[] players = this.getServer().getOnlinePlayers();
        for (Player p : players) {
            MFPersistence.savePlayer(p, this);
        }

        // save chunk data
        List<World> worlds = this.getServer().getWorlds();
        for (World w : worlds) {
            Chunk[] chunks = w.getLoadedChunks();
            for (Chunk c : chunks) {
                MFPersistence.saveChunk(c, this);
            }
        }

    }

}
