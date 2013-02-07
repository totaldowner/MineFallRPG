package com.totaldowner.minefall;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class MFItem {

    public static ItemStack makeFromConfig(String itemName, int amount,
            Plugin plugin) {

        if (plugin.getConfig().getString("items." + itemName + ".look").compareTo("") != 0) {
            Material material;

            // find out what its made of
            String mat = plugin.getConfig().getString("items." + itemName + ".look");
            if (mat.compareTo("woodensword") == 0) {
                material = Material.WOOD_SWORD;

            } else if (mat.compareTo("woodenaxe") == 0) {
                material = Material.WOOD_AXE;

            } else if (mat.compareTo("woodenpick") == 0) {
                material = Material.WOOD_PICKAXE;

            } else if (mat.compareTo("woodenhoe") == 0) {
                material = Material.WOOD_HOE;

            } else if (mat.compareTo("woodenshovel") == 0) {
                material = Material.WOOD_SPADE;
            } else {
                return null;
            }

            String name = plugin.getConfig().getString("items." + itemName + ".name");
            double damage = plugin.getConfig().getDouble("items." + itemName + ".damage");
            double value = plugin.getConfig().getDouble("items." + itemName + ".value");

            List<String> lore = new ArrayList<String>();
            lore.add("damage:" + String.valueOf(damage));
            lore.add("value:" + String.valueOf(value));

            ItemStack item = new ItemStack(material, amount);
            ItemMeta iMeta = item.getItemMeta();
            iMeta.setDisplayName(name);
            iMeta.setLore(lore);
            item.setItemMeta(iMeta);

            return item;
        }

        return null;
    }

    public static double getDamage(ItemStack item) {
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasLore()) {
                List<String> lore = item.getItemMeta().getLore();
                for (String s : lore) {
                    if (s.startsWith("damage:")) {
                        return Double.valueOf(s.substring(7));
                    }
                }
            }
        }
        return 1.0;
    }

    public static double getValue(ItemStack item) {
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasLore()) {
                List<String> lore = item.getItemMeta().getLore();
                for (String s : lore) {
                    if (s.startsWith("value:")) {
                        return Double.valueOf(s.substring(6));
                    }
                }
            }
        }
        return 1.0;
    }
}
