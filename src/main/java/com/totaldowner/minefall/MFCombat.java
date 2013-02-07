package com.totaldowner.minefall;

import java.text.DecimalFormat;
import java.util.Random;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.github.totaldowner.minefall.living.MFLivingThing;
import com.github.totaldowner.minefall.living.MFPlayer;

/**
 * Class that handles the majority of combat situations
 * 
 * @author Timothy Swartz
 * 
 */
public class MFCombat {

    public static void livingDamageLiving(MFLivingThing attacker,
            MFLivingThing defender, DamageCause cause) {
        double damageDealt = 0.0;
        DecimalFormat df = new DecimalFormat("#.##");
        Random rand = new Random();

        if (cause == DamageCause.ENTITY_ATTACK) { // Physical hit
            // Unarmed combat damage
            if (attacker.getCombatSkill().compareTo("unarmed") == 0) {
                damageDealt = ((attacker.getStrength() / 50) + (attacker.getStrength() / 50) * attacker.getSkill("unarmed") * rand.nextDouble() / 100) * attacker.getDamage();
                attacker.gainSkill("unarmed");
                // take armor into account later, maybe other modifiers for mobs

            } else if (attacker.getCombatSkill().compareTo("sword") == 0) {
                damageDealt = ((attacker.getStrength() / 50) + (attacker.getStrength() / 50) * attacker.getSkill("sword") * rand.nextDouble() / 100) * attacker.getDamage();
                attacker.gainSkill("sword");
                // take armor into account later, maybe other modifiers for mobs

            } else if (attacker.getCombatSkill().compareTo("axe") == 0) {
                damageDealt = ((attacker.getStrength() / 50) + (attacker.getStrength() / 50) * attacker.getSkill("axe") * rand.nextDouble() / 100) * attacker.getDamage();
                attacker.gainSkill("axe");
                // take armor into account later, maybe other modifiers for mobs

            } else if (attacker.getCombatSkill().compareTo("shovel") == 0) {
                damageDealt = ((attacker.getStrength() / 50) + (attacker.getStrength() / 50) * attacker.getSkill("shovel") * rand.nextDouble() / 100) * attacker.getDamage();
                attacker.gainSkill("shovel");
                // take armor into account later, maybe other modifiers for mobs

            } else if (attacker.getCombatSkill().compareTo("pickaxe") == 0) {
                damageDealt = ((attacker.getStrength() / 50) + (attacker.getStrength() / 50) * attacker.getSkill("pickaxe") * rand.nextDouble() / 100) * attacker.getDamage();
                attacker.gainSkill("pickaxe");
                // take armor into account later, maybe other modifiers for mobs

            } else if (attacker.getCombatSkill().compareTo("hoe") == 0) {
                damageDealt = ((attacker.getStrength() / 50) + (attacker.getStrength() / 50) * attacker.getSkill("hoe") * rand.nextDouble() / 100) * attacker.getDamage();
                attacker.gainSkill("hoe");
                // take armor into account later, maybe other modifiers for mobs

            }

        } else if (cause == DamageCause.PROJECTILE) {
            // assume archery for now fix scaling later
            damageDealt = ((attacker.getDexterity() / 50) + (attacker.getDexterity() / 50) * attacker.getSkill("archery") * rand.nextDouble() / 100) * attacker.getDamage();
            attacker.gainSkill("archery");
            // take armor into account later, maybe other modifiers for mobs

        } else {
            // probably a creeper, leave it alone until magic happens
            damageDealt = 0;
        }

        defender.setHealth(defender.getHealth() - damageDealt);
        defender.updateHealth();

        // Send messages to players if they were involved
        if (defender instanceof MFPlayer) {
            ((MFPlayer) defender).sendMessage(attacker.getName() + " hit you for " + df.format(damageDealt) + " damage §aHP:" + df.format(defender.getHealth()) + "/" + df.format(defender.getMaxHealth()));
        }
        if (attacker instanceof MFPlayer) {
            ((MFPlayer) attacker).sendMessage("You hit " + defender.getName() + " for " + df.format(damageDealt) + " damage §4HP:" + df.format(defender.getHealth()) + "/" + df.format(defender.getMaxHealth()));
        }
    }

}
