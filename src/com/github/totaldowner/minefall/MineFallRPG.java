package com.github.totaldowner.minefall;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.totaldowner.minefall.command.DebugCommand;
import com.github.totaldowner.minefall.command.MFOpCommand;
import com.github.totaldowner.minefall.command.MineFallCommand;

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

        getCommand("mqdebug").setExecutor(new DebugCommand(this));
        getCommand("minequest").setExecutor(new MineFallCommand(this));
        getCommand("mq").setExecutor(new MineFallCommand(this));
        getCommand("mqop").setExecutor(new MFOpCommand(this));

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
