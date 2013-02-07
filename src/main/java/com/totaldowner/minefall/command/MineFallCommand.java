package com.totaldowner.minefall.command;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.totaldowner.minefall.MFItem;
import com.totaldowner.minefall.MineFallRPG;
import com.totaldowner.minefall.living.MFPlayer;

/**
 * The class for the /mf , /minefall commands
 * 
 * @author Timothy Swartz
 * 
 */
public class MineFallCommand implements CommandExecutor {

    private final MineFallRPG mq;

    /**
     * Constructor for the command
     * 
     * @param mineFallRPG
     *            a reference to the MineQuest RPG plugin object
     */
    public MineFallCommand(MineFallRPG mineFallRPG) {
        this.mq = mineFallRPG;
    }

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        DecimalFormat df = new DecimalFormat("#.##");

        if (arg3.length > 0) {
            if (arg0.getName().compareTo("CONSOLE") == 0) { // Can not be run by
                                                            // a console
                mq.getLogger().info("Command can only be run by a player");

            } else {
                Player player = (Player) arg0;

                if (arg3[0].compareTo("stats") == 0) { // Report stats to player
                    MFPlayer p = new MFPlayer((Player) arg0, mq);
                    arg0.sendMessage("Stats: Str-" + df.format(p.getStrength()) + " Dex-" + df.format(p.getDexterity()) + " Int-" + df.format(p.getIntelligence()));
                    arg0.sendMessage("Skills: Unarmed-" + df.format(p.getSkill("unarmed")) + " Sword-" + df.format(p.getSkill("sword")) + " Archery-" + df.format(p.getSkill("archery")));
                    arg0.sendMessage("        Pickaxe-" + df.format(p.getSkill("pickaxe")) + " Axe-" + df.format(p.getSkill("axe")) + " Shovel-" + df.format(p.getSkill("shovel")));

                } else if (arg3[0].compareTo("iteminfo") == 0) { // Display some
                                                                 // item info
                    ItemStack item = player.getItemInHand();

                    player.sendMessage("Name: " + item.getItemMeta().getDisplayName());
                    player.sendMessage("Damage: " + MFItem.getDamage(item));
                    player.sendMessage("Value: " + MFItem.getValue(item));

                } else if (arg3[0].compareTo("basics") == 0) { // give player
                                                               // basic command
                                                               // items

                    if (player.getInventory().firstEmpty() != -1) {
                        ItemStack item = new ItemStack(Material.COMPASS, 1);
                        ItemMeta iMeta = item.getItemMeta();
                        iMeta.setDisplayName(ChatColor.DARK_RED + "Consider Target");
                        item.setItemMeta(iMeta);
                        player.getInventory().setItem(player.getInventory().firstEmpty(), item);
                        player.sendMessage("You have been given the basic command items: " + item.getItemMeta().getDisplayName());

                    } else {
                        player.sendMessage("No room in inventory");
                    }
                }
                return true;
            }

        }
        return false;
    }

}
