package com.totaldowner.minefall.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.totaldowner.minefall.MineFallRPG;

/**
 * Class for the /mqdebug command
 * 
 * @author Timothy Swartz
 * 
 */
public class DebugCommand implements CommandExecutor {

    private final MineFallRPG mq;

    /**
     * Constructor for the command
     * 
     * @param mineQuestRPG
     *            a reference to the MineQuest RPG plugin object
     */
    public DebugCommand(MineFallRPG mineQuestRPG) {
        this.mq = mineQuestRPG;
    }

    public MineFallRPG getPlugin() {
        return this.mq;
    }

    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if (arg3.length > 0) {
        }
        return false;
    }

}
