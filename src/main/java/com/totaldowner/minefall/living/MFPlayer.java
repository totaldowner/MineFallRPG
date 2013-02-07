package com.totaldowner.minefall.living;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Class that defines and manages the metadata for a player
 * 
 * @author Timothy Swartz
 * 
 */
public class MFPlayer extends MFLivingThing {

    private Player player;

    /**
     * Constructor for a player
     * 
     * @param player
     *            the player to reference
     * @param plugin
     *            the plugin
     */
    public MFPlayer(Player player, Plugin plugin) {
        super(player, plugin);
        this.player = (Player) player;
    }

    /**
     * Sets the players metadata to a default state
     */
    public void createNewPlayer() {
        this.setName(player.getName());
        // / Add stats here

        this.setStrength(this.getPlugin().getConfig().getDouble("defaultplayer.strength"));
        this.setStamina(this.getPlugin().getConfig().getDouble("defaultplayer.stamina"));
        this.setDexterity(this.getPlugin().getConfig().getDouble("defaultplayer.dexterity"));
        this.setIntelligence(this.getPlugin().getConfig().getDouble("defaultplayer.intelligence"));
        this.setWisdom(this.getPlugin().getConfig().getDouble("defaultplayer.wisdom"));
        this.setHealth(this.getMaxHealth());
        this.updateHealth();

        this.setSkill("unarmed", this.getPlugin().getConfig().getDouble("defaultplayer.skills.unarmed"));
        this.setSkill("sword", this.getPlugin().getConfig().getDouble("defaultplayer.skills.sword"));
        this.setSkill("pickaxe", this.getPlugin().getConfig().getDouble("defaultplayer.skills.pickaxe"));
        this.setSkill("axe", this.getPlugin().getConfig().getDouble("defaultplayer.skills.axe"));
        this.setSkill("shovel", this.getPlugin().getConfig().getDouble("defaultplayer.skills.shovel"));
        this.setSkill("hoe", this.getPlugin().getConfig().getDouble("defaultplayer.skills.hoe"));
        this.setSkill("heavyarmor", this.getPlugin().getConfig().getDouble("defaultplayer.skills.heavyarmor"));
        this.setSkill("lightarmor", this.getPlugin().getConfig().getDouble("defaultplayer.skills.lightarmor"));
        this.setSkill("mining", this.getPlugin().getConfig().getDouble("defaultplayer.skills.mining"));
        this.setSkill("woodcutting", this.getPlugin().getConfig().getDouble("defaultplayer.skills.woodcutting"));
        this.setSkill("digging", this.getPlugin().getConfig().getDouble("defaultplayer.skills.digging"));
        this.setSkill("farming", this.getPlugin().getConfig().getDouble("defaultplayer.skills.farming"));
    }

    /**
     * Sends a message to the player through chat
     * 
     * @param message
     *            the message to send
     */
    public void sendMessage(String message) {
        this.player.sendMessage(message);
    }

    /**
     * Gets a target withing the players line of sight
     * 
     * @param entities
     *            a list of entities to check for line of sight with
     * @return the best match target within the players line of sight
     * 
     */
    public <T extends Entity> T getTarget(final Iterable<T> entities) {
        if (player == null)
            return null;
        T target = null;
        double targetDistanceSquared = Double.MAX_VALUE;
        final double radiusSquared = 1.5;
        final Vector l = player.getEyeLocation().toVector(), n = player.getLocation().getDirection().normalize();
        final double cos = Math.cos(Math.PI / 4);
        for (final T other : entities) {
            if (other == player)
                continue;
            if (target == null || targetDistanceSquared > other.getLocation().distanceSquared(player.getLocation())) {
                final Vector t = other.getLocation().toVector().subtract(l);
                if (n.clone().crossProduct(t).lengthSquared() < radiusSquared && t.normalize().dot(n) >= cos) {
                    target = other;
                    targetDistanceSquared = target.getLocation().distanceSquared(player.getLocation());
                }
            }
        }
        return target;
    }

    /**
     * Checks players line of sight for a target and returns some information on
     * the target
     */
    public void considerTarget() {
        List<Entity> potentialTargets = player.getNearbyEntities(30, 30, 30); // get
                                                                              // nearby
                                                                              // entities
        List<Entity> finalPotentials = new ArrayList<Entity>();

        for (Entity e : potentialTargets) {
            if (e instanceof Creature || e instanceof Player) {
                finalPotentials.add(e);
            }
        }
        Entity entity = this.getTarget(finalPotentials); // get target within
                                                         // range
        if (entity == null) {
            player.sendMessage("No target not found");

        } else if (entity instanceof Creature) {
            MFMob mob = new MFMob((Creature) entity, this.getPlugin());
            player.sendMessage("Target: " + mob.getName() + " §4HP:" + mob.getHealth() + "/" + mob.getMaxHealth());

        } else if (entity instanceof Player) {

            MFPlayer p = new MFPlayer((Player) entity, this.getPlugin());
            player.sendMessage("Target: " + p.getName() + " §aHP:" + p.getHealth() + "/" + p.getMaxHealth());
        }
    }

    public void changePlayerSkill(Material material) {

        if (material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD || material == Material.STONE_SWORD || material == Material.GOLD_SWORD || material == Material.WOOD_SWORD) {

            this.setCombatSkill("sword");

        } else if (material == Material.IRON_AXE || material == Material.DIAMOND_AXE || material == Material.STONE_AXE || material == Material.GOLD_AXE || material == Material.WOOD_AXE) {

            this.setCombatSkill("axe");

        } else if (material == Material.IRON_PICKAXE || material == Material.DIAMOND_PICKAXE || material == Material.STONE_PICKAXE || material == Material.GOLD_PICKAXE || material == Material.WOOD_PICKAXE) {

            this.setCombatSkill("pickaxe");

        } else if (material == Material.IRON_HOE || material == Material.DIAMOND_HOE || material == Material.STONE_HOE || material == Material.GOLD_HOE || material == Material.WOOD_HOE) {

            this.setCombatSkill("hoe");

        } else if (material == Material.IRON_SPADE || material == Material.DIAMOND_SPADE || material == Material.STONE_SPADE || material == Material.GOLD_SPADE || material == Material.WOOD_SPADE) {

            this.setCombatSkill("shovel");

        } else {

            this.setCombatSkill("unarmed");
        }

    }
}
