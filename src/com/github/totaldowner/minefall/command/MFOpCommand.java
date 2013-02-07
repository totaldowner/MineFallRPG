package com.github.totaldowner.minefall.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.totaldowner.minefall.MFItem;
import com.github.totaldowner.minefall.MineFallRPG;

public class MFOpCommand implements CommandExecutor {

    private final MineFallRPG mq;

    /**
     * Constructor for the command
     * 
     * @param mineQuestRPG
     *            a reference to the MineQuest RPG plugin object
     */
    public MFOpCommand(MineFallRPG mineQuestRPG) {
        this.mq = mineQuestRPG;
    }

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
            String[] arg3) {

        if (arg3.length > 0) {
            if (arg0.getName().compareTo("CONSOLE") == 0) { // Can not be run by
                                                            // a console
                mq.getLogger().info("Command can only be run by a player");
                return true;
            } else {
                Player player = (Player) arg0;
                if (arg3[0].compareTo("weapons") == 0) {

                    player.getInventory().setItem(player.getInventory().firstEmpty(), new ItemStack(Material.DIAMOND_SWORD));
                    player.getInventory().setItem(player.getInventory().firstEmpty(), new ItemStack(Material.DIAMOND_SPADE));
                    player.getInventory().setItem(player.getInventory().firstEmpty(), new ItemStack(Material.DIAMOND_PICKAXE));
                    player.getInventory().setItem(player.getInventory().firstEmpty(), new ItemStack(Material.DIAMOND_AXE));
                    player.getInventory().setItem(player.getInventory().firstEmpty(), new ItemStack(Material.DIAMOND_HOE));

                    return true;
                } else if (arg3.length > 1) {
                    if (arg3[0].compareTo("item") == 0) {
                        String itemName = arg3[1];

                        if (arg3.length > 2 && Integer.valueOf(arg3[2]) > 0) {
                            player.getInventory().setItem(player.getInventory().firstEmpty(), MFItem.makeFromConfig(itemName, Integer.valueOf(arg3[2]), mq));
                        } else {
                            player.getInventory().setItem(player.getInventory().firstEmpty(), MFItem.makeFromConfig(itemName, 1, mq));
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
