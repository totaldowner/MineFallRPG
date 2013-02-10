package com.totaldowner.minefall;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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
import org.bukkit.inventory.ItemStack;

import com.totaldowner.minefall.living.MFLivingThing;
import com.totaldowner.minefall.living.MFMob;
import com.totaldowner.minefall.living.MFPlayer;

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
    public void onEntityDamage(EntityDamageEvent event){
        
        if(event.getCause() == DamageCause.STARVATION){ //Starvation hurts differently
            if(event.getEntity() instanceof Player){
                MFPlayer player = new MFPlayer((Player) event.getEntity(), mq);
                
                if(player.getMaxHealth() != 0 && player.getHealth() / player.getMaxHealth() > 0.5 ){ //take damage till %50
                    player.setHealth(player.getHealth() - player.getMaxHealth() * 0.5);
                    player.updateHealth();
                }
            }
            event.setDamage(0);
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
    
    //Set to highest so it can let world guard style plugins prevent breakage
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event){
        
        //if it wasnt cancelled already by something else
        if(!event.isCancelled()){
            
            int itemid = event.getBlock().getTypeId();
            int itemsubid = event.getBlock().getData();

            Random rand = new Random();
            List<String> itemNames = mq.getConfig().getStringList("blocks.i" + String.format("%02X", itemid) + ".breakitem");
            List<Double> itemChances = mq.getConfig().getDoubleList("blocks.i" + String.format("%02X", itemid) + ".breakitemchance");
            
            itemNames.addAll(mq.getConfig().getStringList("blocks.i" + String.format("%02X", itemid) + ".s" + String.format("%02X", itemsubid) + ".breakitem"));
            itemChances.addAll(mq.getConfig().getDoubleList("blocks.i" + String.format("%02X", itemid) + ".s" + String.format("%02X", itemsubid) + ".breakitemchance"));

            for(int x = 0; x < itemNames.size() && x < itemChances.size(); x++){
                String itemName = itemNames.get(x);
                Double itemChance = itemChances.get(x);
                ItemStack item;
                
                item = MFItem.makeFromConfig(itemName, 1, mq);
                if(item != null){
                    if(rand.nextDouble() < itemChance){
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
                    }
                }
                
            }

            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event) {
        MFPlayer p = new MFPlayer(event.getPlayer(), mq);
        p.changePlayerSkill(null);
        p.setDamage(mq.getConfig().getDouble("globals.defaultplayer.unarmeddamage", 0.5));
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
        if(event.getPlayer().getInventory().getItem(event.getNewSlot()) != null){
            p.changePlayerSkill(event.getPlayer().getInventory().getItem(event.getNewSlot()).getType());
            p.setDamage(MFItem.getDamage(event.getPlayer().getInventory().getItem(event.getNewSlot())));
        } else {
            p.setDamage(mq.getConfig().getDouble("globals.defaultplayer.unarmeddamage", 0.5));
        }
    }

    @EventHandler
    public void onCreatureSpawned(CreatureSpawnEvent event) {

        if (event.getEntity() instanceof Creature) {
            MFMob.loadDefaultForType((Creature) event.getEntity(), mq);
        }

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity() instanceof Creature){
            event.getDrops().clear();   //clear the current items
            
            Random rand = new Random();
            MFMob mob = new MFMob((Creature) event.getEntity(), mq);
            List<String> itemNames = mq.getConfig().getStringList("mobs." + mob.getMobType() + ".loot.itemnames");
            List<Double> itemChances = mq.getConfig().getDoubleList("mobs." + mob.getMobType() + ".loot.itemchances");
            
            for(int x = 0; x < itemNames.size() && x < itemChances.size(); x++){
                String itemName = itemNames.get(x);
                Double itemChance = itemChances.get(x);
                ItemStack item;
                
                item = MFItem.makeFromConfig(itemName, 1, mq);
                if(item != null){
                    if(rand.nextDouble() < itemChance){
                        event.getDrops().add(item);
                    }
                }
                
            }
        }
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
