package com.github.totaldowner.minefall.living;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/**
 * Class that defines a living objects meta data.
 * 
 * @author Timothy Swartz
 * 
 */
public abstract class MFLivingThing {

    private LivingEntity entity;
    private Plugin plugin;

    /**
     * General constructor for a living entity
     * 
     * @param entity
     *            the entity to access metadata for
     * @param plugin
     *            a reference to the plugin
     */
    public MFLivingThing(LivingEntity entity, Plugin plugin) {
        this.entity = entity;
        this.plugin = plugin;
    }

    /**
     * Gets the entity associated with this object
     * 
     * @return the Entity associated with this object
     */
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * Gets the plugin associated with this object
     * 
     * @return the plugin associated with this object
     */
    public Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Sets the name metadata for the entity
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.entity.setMetadata("MFName", new FixedMetadataValue(plugin, name));
    }

    /**
     * Gets the name metadata for the entity
     * 
     * @return the name of the entity
     */
    public String getName() {
        List<MetadataValue> values = entity.getMetadata("MFName");
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return (String) value.value();
            }
        }
        return "";
    }

    /**
     * Sets the health of the entity
     * 
     * {@link updateHealth()} should be called to reflect this in the game.
     * 
     * @param health
     *            the health to set the entity to
     */
    public void setHealth(double health) {
        this.entity.setMetadata("MFHealth", new FixedMetadataValue(plugin, health));
    }

    /**
     * Gets the current health of the entity
     * 
     * @return the current health of the entity
     */
    public double getHealth() {
        List<MetadataValue> values = entity.getMetadata("MFHealth");
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return (Double) value.value();
            }
        }
        return 0;
    }

    /**
     * Gets the maximum health this entity can have. Calculated from strength
     * and stamina
     * 
     * @return the maximum health this entity can have
     */
    public double getMaxHealth() {
        double value = this.getStamina() * 2 + this.getStrength();
        return value;
    }

    /**
     * Reflects the changes in health and maxHealth within the game
     */
    public void updateHealth() {
        double health = this.getHealth();
        double maxHealth = this.getMaxHealth();

        if (maxHealth > 0) {
            // Scale the health to appear on the healthbar properly
            Double finalHealth = Math.ceil((double) health * (double) entity.getMaxHealth() / (double) maxHealth);
            if (finalHealth <= 0) {
                finalHealth = 0.0;

                this.entity.setHealth(1);
                this.entity.damage(1);

            } else {
                this.entity.setHealth(finalHealth.intValue());
            }
        } else {
            // Must not be loaded, so load it
            if (this.getEntity() instanceof Creature) {
                MFMob.loadDefaultForType((Creature) this.getEntity(), plugin);
            } else if (this.getEntity() instanceof Player) {
                MFPlayer tmp = new MFPlayer((Player) this.getEntity(), plugin);
                tmp.createNewPlayer();

            }
        }
    }

    /**
     * Sets a skill to a specified value
     * 
     * @param skillName
     *            the name of the skill to set
     * @param skillValue
     *            the value to set the skill to
     */
    public void setSkill(String skillName, double skillValue) {
        this.entity.setMetadata("MFSkill" + skillName, new FixedMetadataValue(plugin, skillValue));
    }

    /**
     * Gets a skills value
     * 
     * @param skillName
     *            the name of the skill to get the value from
     * @return the skill's value
     */
    public double getSkill(String skillName) {
        List<MetadataValue> values = entity.getMetadata("MFSkill" + skillName);
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return (Double) value.value();
            }
        }
        return 0;
    }

    public void setStrength(double strength) {
        this.entity.setMetadata("MFStrength", new FixedMetadataValue(plugin, strength));
    }

    public double getStrength() {
        List<MetadataValue> values = entity.getMetadata("MFStrength");
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return (Double) value.value();
            }
        }
        return 0;
    }

    public void setDexterity(double dexterity) {
        this.entity.setMetadata("MFDexterity", new FixedMetadataValue(plugin, dexterity));
    }

    public double getDexterity() {
        List<MetadataValue> values = entity.getMetadata("MFDexterity");
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return (Double) value.value();
            }
        }
        return 0;
    }

    public void setStamina(double stamina) {
        this.entity.setMetadata("MFStamina", new FixedMetadataValue(plugin, stamina));
    }

    public double getStamina() {
        List<MetadataValue> values = entity.getMetadata("MFStamina");
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return (Double) value.value();
            }
        }
        return 0;
    }

    public void setIntelligence(double intelligence) {
        this.entity.setMetadata("MFIntelligence", new FixedMetadataValue(plugin, intelligence));
    }

    public double getIntelligence() {
        List<MetadataValue> values = entity.getMetadata("MFIntelligence");
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return (Double) value.value();
            }
        }
        return 0;
    }

    public void setWisdom(double wisdom) {
        this.entity.setMetadata("MFWisdom", new FixedMetadataValue(plugin, wisdom));
    }

    public double getWisdom() {
        List<MetadataValue> values = entity.getMetadata("MFWisdom");
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return (Double) value.value();
            }
        }
        return 0;
    }

    public void setDamage(double damage) {
        this.entity.setMetadata("MFDamage", new FixedMetadataValue(plugin, damage));
    }

    public double getDamage() {
        List<MetadataValue> values = entity.getMetadata("MFDamage");
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return (Double) value.value();
            }
        }
        return 1.0;
    }

    /**
     * The current skill for physical combat this entity is using
     * 
     * @param skillName
     *            the name of the skill this entity is using for combat
     */
    public void setCombatSkill(String skillName) {
        this.entity.setMetadata("MFCombatSkill", new FixedMetadataValue(plugin, skillName));
    }

    /**
     * Gets the current skill name for physical combat
     * 
     * @return
     */
    public String getCombatSkill() {
        List<MetadataValue> values = entity.getMetadata("MFCombatSkill");
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return (String) value.value();
            }
        }
        return "";
    }

    /**
     * Gains skill
     * 
     * @param skill
     */
    public void gainSkill(String skill) {
        // Gain stats too
        double strengthgain = 0.0;
        double staminagain = 0.0;
        double intelligencegain = 0.0;
        double dexteritygain = 0.0;
        double wisdomgain = 0.0;
        double gain = 0.0;

        if (skill.compareTo("unarmed") == 0) {
            gain = 1.0;
            strengthgain = 0.1;
            staminagain = 0.05;

        } else if (skill.compareTo("sword") == 0) {
            gain = 1.0;
            strengthgain = 0.1;
            staminagain = 0.05;

        } else if (skill.compareTo("archery") == 0) {
            gain = 1.0;
            dexteritygain = 0.1;
            strengthgain = 0.05;

        } else if (skill.compareTo("axe") == 0) {
            gain = 1.0;
            strengthgain = 0.1;
            staminagain = 0.05;

        } else if (skill.compareTo("pickaxe") == 0) {
            gain = 1.0;
            strengthgain = 0.1;
            staminagain = 0.05;

        } else if (skill.compareTo("shovel") == 0) {
            gain = 1.0;
            strengthgain = 0.1;
            staminagain = 0.05;

        } else if (skill.compareTo("hoe") == 0) {
            gain = 1.0;
            strengthgain = 0.1;
            staminagain = 0.05;

        }

        double gainAmount = this.getSkill(skill);
        Random rand = new Random();
        if (gainAmount <= 1.0)
            gainAmount = 1.0;
        gainAmount = 1 / Math.pow(gainAmount, 1.5);
        gainAmount = gainAmount * rand.nextFloat() * 2.0;
        this.setSkill(skill, gainAmount * gain + this.getSkill(skill));

        // Strength
        gainAmount = this.getStrength();
        if (gainAmount <= 1.0)
            gainAmount = 1.0;
        gainAmount = 1 / Math.pow(gainAmount, 1.5);
        gainAmount = gainAmount * rand.nextFloat() * 2.0;
        this.setStrength(this.getStrength() + gainAmount * strengthgain);

        // Stamina
        gainAmount = this.getStamina();
        if (gainAmount <= 1.0)
            gainAmount = 1.0;
        gainAmount = 1 / Math.pow(gainAmount, 1.5);
        gainAmount = gainAmount * rand.nextFloat() * 2.0;
        this.setStrength(this.getStamina() + gainAmount * staminagain);

        // Dexterity
        gainAmount = this.getDexterity();
        if (gainAmount <= 1.0)
            gainAmount = 1.0;
        gainAmount = 1 / Math.pow(gainAmount, 1.5);
        gainAmount = gainAmount * rand.nextFloat() * 2.0;
        this.setStrength(this.getDexterity() + gainAmount * dexteritygain);

        // Intelligence
        gainAmount = this.getIntelligence();
        if (gainAmount <= 1.0)
            gainAmount = 1.0;
        gainAmount = 1 / Math.pow(gainAmount, 1.5);
        gainAmount = gainAmount * rand.nextFloat() * 2.0;
        this.setStrength(this.getIntelligence() + gainAmount * intelligencegain);

        // Wisdom
        gainAmount = this.getWisdom();
        if (gainAmount <= 1.0)
            gainAmount = 1.0;
        gainAmount = 1 / Math.pow(gainAmount, 1.5);
        gainAmount = gainAmount * rand.nextFloat() * 2.0;
        this.setStrength(this.getWisdom() + gainAmount * wisdomgain);
    }
}
