package com.totaldowner.minefall.living;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

/**
 * Class for a Creature Mob to use. Defines the metadata used by mobs.
 * 
 * @author Timothy Swartz
 * 
 */
public class MFMob extends MFLivingThing {

    private Creature creature;

    /**
     * Creates a MQMob with given creature as the origin.
     * 
     * @param creature
     *            the creature to reference
     * @param plugin
     *            the plugin
     */
    public MFMob(Creature creature, Plugin plugin) {
        super(creature, plugin);
        this.creature = (Creature) creature;
    }

    public Creature getCreature() {
        return this.creature;
    }

    /**
     * Makes the mob have the metadata supplied by the config.yml
     * 
     * @param mobtype
     *            the key for the mob you want this to be
     */
    public void loadFromConfig(String mobtype) {

        String mobname = this.getPlugin().getConfig().getString("mobs." + mobtype + ".name");

        if (mobname != null) {
            this.setName(mobname);
            this.setStrength(this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".strength"));
            this.setStamina(this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".stamina"));
            this.setDexterity(this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".dexterity"));
            this.setIntelligence(this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".intelligence"));
            this.setWisdom(this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".wisdom"));
            this.setHealth(this.getMaxHealth());
            this.updateHealth();

            // Misc
            this.setCombatSkill(this.getPlugin().getConfig().getString("mobs." + mobtype + ".combatskill"));
            this.setDamage(this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".damage"));

            // Skills
            this.setSkill("unarmed", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.unarmed"));
            this.setSkill("sword", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.sword"));
            this.setSkill("pickaxe", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.pickaxe"));
            this.setSkill("axe", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.axe"));
            this.setSkill("shovel", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.shovel"));
            this.setSkill("hoe", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.hoe"));
            this.setSkill("heavyarmor", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.heavyarmor"));
            this.setSkill("lightarmor", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.lightarmor"));

            // Not needed, but here for consistency
            this.setSkill("mining", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.mining"));
            this.setSkill("woodcutting", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.woodcutting"));
            this.setSkill("digging", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.digging"));
            this.setSkill("farming", this.getPlugin().getConfig().getDouble("mobs." + mobtype + ".skills.farming"));

        } else {
            this.getPlugin().getLogger().info("Unknown mob type or name not set: " + mobtype);
        }

    }

    /**
     * Loads a default set of metadata for the mob from the config.yml
     * 
     * @param creature
     *            the creature this is for
     * @param plugin
     *            the plugin
     */
    public static void loadDefaultForType(Creature creature, Plugin plugin) {
        EntityType type = creature.getType();
        MFMob mob = new MFMob(creature, plugin);

        if (type == EntityType.BAT) {
            mob.loadFromConfig("bat");

        } else if (type == EntityType.BLAZE) {
            mob.loadFromConfig("blaze");

        } else if (type == EntityType.CAVE_SPIDER) {
            mob.loadFromConfig("cavespider");

        } else if (type == EntityType.CHICKEN) {
            mob.loadFromConfig("chicken");

        } else if (type == EntityType.COW) {
            mob.loadFromConfig("cow");

        } else if (type == EntityType.CREEPER) {
            mob.loadFromConfig("creeper");

        } else if (type == EntityType.ENDERMAN) {
            mob.loadFromConfig("enderman");

        } else if (type == EntityType.ENDER_DRAGON) {
            mob.loadFromConfig("enderdragon");

        } else if (type == EntityType.GHAST) {
            mob.loadFromConfig("ghast");

        } else if (type == EntityType.IRON_GOLEM) {
            mob.loadFromConfig("irongolem");

        } else if (type == EntityType.MAGMA_CUBE) {
            mob.loadFromConfig("magmacube");

        } else if (type == EntityType.MUSHROOM_COW) {
            mob.loadFromConfig("mushroomcow");

        } else if (type == EntityType.OCELOT) {
            mob.loadFromConfig("ocelot");

        } else if (type == EntityType.PIG) {
            mob.loadFromConfig("pig");

        } else if (type == EntityType.PIG_ZOMBIE) {
            mob.loadFromConfig("pigzombie");

        } else if (type == EntityType.SHEEP) {
            mob.loadFromConfig("sheep");

        } else if (type == EntityType.SILVERFISH) {
            mob.loadFromConfig("silverfish");

        } else if (type == EntityType.SKELETON) {
            mob.loadFromConfig("skeleton");

        } else if (type == EntityType.SLIME) {
            mob.loadFromConfig("slime");

        } else if (type == EntityType.SNOWMAN) {
            mob.loadFromConfig("snowman");

        } else if (type == EntityType.SPIDER) {
            mob.loadFromConfig("spider");

        } else if (type == EntityType.SQUID) {
            mob.loadFromConfig("squid");

        } else if (type == EntityType.VILLAGER) {
            mob.loadFromConfig("villager");

        } else if (type == EntityType.WITCH) {
            mob.loadFromConfig("witch");

        } else if (type == EntityType.WITHER) {
            mob.loadFromConfig("wither");

        } else if (type == EntityType.WOLF) {
            mob.loadFromConfig("wolf");

        } else if (type == EntityType.ZOMBIE) {
            mob.loadFromConfig("zombie");

        }
    }
}
