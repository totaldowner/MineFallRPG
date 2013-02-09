package com.totaldowner.minefall;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class MFItem {

    public static ItemStack makeFromConfig(String itemName, int amount, Plugin plugin) {

        
        
        if(plugin.getConfig().getString("items." + itemName + ".look") == null){
            return null;
        
        } else if(plugin.getConfig().getString("items." + itemName + ".look").compareTo("") != 0) {
            Material material;

            // find out what its made of
            String mat = plugin.getConfig().getString("items." + itemName + ".look");
            //WEAPONS
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
                
            //FOOD
            } else if (mat.compareTo("apple") == 0) {
                material = Material.APPLE;
                
            } else if (mat.compareTo("bakedpotato") == 0) {
                material = Material.BAKED_POTATO;
                
            } else if (mat.compareTo("bread") == 0) {
                material = Material.BREAD;
                
            } else if (mat.compareTo("cake") == 0) {
                material = Material.CAKE;
                
            } else if (mat.compareTo("carrot") == 0) {
                material = Material.CARROT;
                
            } else if (mat.compareTo("cookedchicken") == 0) {
                material = Material.COOKED_CHICKEN;
                
            } else if (mat.compareTo("cookedfish") == 0) {
                material = Material.COOKED_FISH;
                
            } else if (mat.compareTo("cookedporkchop") == 0) {
                material = Material.GRILLED_PORK;
                
            } else if (mat.compareTo("cookie") == 0) {
                material = Material.COOKIE;
                
            } else if (mat.compareTo("goldenapple") == 0) {
                material = Material.GOLDEN_APPLE;
                
            } else if (mat.compareTo("goldencarrot") == 0) {
                material = Material.GOLDEN_CARROT;
                
            } else if (mat.compareTo("melonslice") == 0) {
                material = Material.MELON;
                
            } else if (mat.compareTo("mushroomstew") == 0) {
                material = Material.MUSHROOM_SOUP;
                
            } else if (mat.compareTo("poisonouspotato") == 0) {
                material = Material.POISONOUS_POTATO;
                
            } else if (mat.compareTo("potato") == 0) {
                material = Material.POTATO_ITEM;
                
            } else if (mat.compareTo("pumpkinpie") == 0) {
                material = Material.PUMPKIN_PIE;
                
            } else if (mat.compareTo("rawbeef") == 0) {
                material = Material.RAW_BEEF;
                
            } else if (mat.compareTo("rawchicken") == 0) {
                material = Material.RAW_CHICKEN;
                
            } else if (mat.compareTo("rawfish") == 0) {
                material = Material.RAW_FISH;
                
            } else if (mat.compareTo("rawporkchop") == 0) {
                material = Material.PORK;
                
            } else if (mat.compareTo("rottenflesh") == 0) {
                material = Material.ROTTEN_FLESH;
                
            } else if (mat.compareTo("spidereye") == 0) {
                material = Material.SPIDER_EYE;
                
            } else if (mat.compareTo("steak") == 0) {
                material = Material.COOKED_BEEF;
                
            } else {
                return null;
            }

            String name = plugin.getConfig().getString("items." + itemName + ".name");
            double damage = plugin.getConfig().getDouble("items." + itemName + ".damage");
            double value = plugin.getConfig().getDouble("items." + itemName + ".value");

            List<String> lore = new ArrayList<String>();
            
            //dont add damage if its 0
            if(damage > 0)
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
        return 0.5;
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
