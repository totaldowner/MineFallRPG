package com.github.totaldowner.minefall;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;

import com.github.totaldowner.minefall.living.MFLivingThing;
import com.github.totaldowner.minefall.living.MFMob;
import com.github.totaldowner.minefall.living.MFPlayer;

/**
 * Class that handles Events sent from Bukkit
 * 
 * @author Timothy Swartz
 * 
 */
public class MFEventListener implements Listener {

    private final MineFallRPG mq;

    public MFEventListener(MineFallRPG mq) {
        this.mq = mq;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        MFPersistence.saveChunk(event.getChunk(), mq);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        MFPersistence.loadChunk(event.getChunk(), mq);
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {

        // Save chunk based data
        List<World> worlds = mq.getServer().getWorlds();
        for (World w : worlds) {
            Chunk[] chunks = w.getLoadedChunks();
            for (Chunk c : chunks) {
                MFPersistence.saveChunk(c, mq);
            }
        }

        Player[] players = mq.getServer().getOnlinePlayers();
        for (Player p : players) {
            MFPersistence.savePlayer(p, mq);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        MFLivingThing attacker = null;
        MFLivingThing defender = null;

        // Sort out players from mobs
        if (event.getDamager() instanceof Player) {
            attacker = new MFPlayer((Player) event.getDamager(), mq);

        } else if (event.getDamager() instanceof Creature) {
            attacker = new MFMob((Creature) event.getDamager(), mq);

        } else if (event.getDamager() instanceof Arrow) {
            Arrow a = (Arrow) event.getDamager();

            if (a.getShooter() instanceof Player) {
                attacker = new MFPlayer((Player) a.getShooter(), mq);

            } else if (a.getShooter() instanceof Creature) {
                attacker = new MFMob((Creature) a.getShooter(), mq);
            }
        }

        // figure out the recieving end
        if (event.getEntity() instanceof Player) {
            defender = new MFPlayer((Player) event.getEntity(), mq);

        } else if (event.getEntity() instanceof Creature) {
            defender = new MFMob((Creature) event.getEntity(), mq);
        }

        // dole out the punishment
        if (attacker != null && defender != null) {
            MFCombat.livingDamageLiving(attacker, defender, event.getCause());
            event.setDamage(0);
        }

    }

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event) {
        MFPlayer p = new MFPlayer(event.getPlayer(), mq);
        p.changePlayerSkill(null);
        p.setDamage(0.5);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        MFPlayer p = new MFPlayer((Player) event.getPlayer(), mq);

        // Set the type of melee weapon being used
        p.changePlayerSkill(event.getPlayer().getInventory().getItemInHand().getType());
        p.setDamage(MFItem.getDamage(event.getPlayer().getInventory().getItemInHand()));
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        MFPlayer p = new MFPlayer(event.getPlayer(), mq);

        // Set the type of melee weapon being used
        p.changePlayerSkill(event.getPlayer().getInventory().getItem(event.getNewSlot()).getType());
        p.setDamage(MFItem.getDamage(event.getPlayer().getInventory().getItem(event.getNewSlot())));
    }

    @EventHandler
    public void onCreatureSpawned(CreatureSpawnEvent event) {

        if (event.getEntity() instanceof Creature) {
            MFMob.loadDefaultForType((Creature) event.getEntity(), mq);
        }

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Load player data
        Player player = event.getPlayer();
        MFPersistence.loadPlayer(player, mq);

        // Check for new player (change to a flag or something)
        MFPlayer p = new MFPlayer(player, mq);
        if (p.getMaxHealth() <= 0)
            p.createNewPlayer();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Save player data
        MFPersistence.savePlayer(event.getPlayer(), mq);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        MFPlayer p = new MFPlayer(player, mq);

        p.setHealth(p.getMaxHealth());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName() && event.getItem().getItemMeta().getDisplayName().compareTo(ChatColor.DARK_RED + "Consider Target") == 0) {
                MFPlayer p = new MFPlayer(player, mq);
                p.considerTarget();
                event.setCancelled(true);
            }
        }
    }
}
