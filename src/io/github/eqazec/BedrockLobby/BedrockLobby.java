/*
 * BedrockLobby - a custom bukkit plugin
 */
package io.github.eqazec.bedrocklobby;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class BedrockLobby
    extends JavaPlugin
    implements Listener
{
    public static final int BUKKIT_VERSION = 3020;

    private transient List<String> cloakToggled = new ArrayList<String>();

    private transient List<String> cloakSpammers = new ArrayList<String>();

    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults( true );
        saveConfig();
        getServer().getPluginManager().registerEvents( this, this );
        getLogger().info( "Enabled!" );
    }

    @Override
    public void onDisable()
    {
        saveConfig();
        getLogger().info( "Disabled!" );
    }

    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent e )
    {

        Player p = e.getPlayer();
        Inventory i = p.getInventory();

        // Sends the Join MOTD if enabled;
        if ( getConfig().getBoolean( "SEND_MOTD" ) )
        {
            p.sendMessage( ChatColor.translateAlternateColorCodes( '&', getConfig().getString( "JOIN_MOTD" ) ) );
        }

        // Clears player's inventory on join if that mechanic is enabled and the
        // player is not exempt.
        if ( getConfig().getBoolean( "CLEAR_INV_ON_JOIN" ) && !p.hasPermission( "bl.invclearexempt" ) )
        {
            i.clear();
        }

        // If enabled, gives player potion effects per configured settings
        if ( getConfig().getBoolean( "POTIONS_ENABLED" ) )
        {
            handlePotions( p );
        }

        // Gives the player 1 cloak Item
        if ( getConfig().getBoolean( "CLOAK_ENABLED" ) )
        {
            giveCloak( i );
        }
    }

    @EventHandler
    public void onPlayerQuit( PlayerQuitEvent e )
    {
        cloakToggled.remove( e.getPlayer() );
        cloakSpammers.remove( e.getPlayer() );
    }

    Material cloakMaterial = Material.getMaterial( getConfig().getString( "CLOAK_MATERIAL_TYPE" ) );

    @EventHandler( priority = EventPriority.HIGH )
    public void onPlayerInteract( PlayerInteractEvent e )
    {
        Player player = e.getPlayer();
        if ( ( ( e.getAction().equals( Action.RIGHT_CLICK_AIR ) ) || ( e.getAction().equals( Action.RIGHT_CLICK_BLOCK ) ) )
            && ( player.getInventory().getItemInHand().getType() == cloakMaterial ) )
        {
            toggleCloak( player );
        }
    }

    /**
     * Causes all other players on the server to "vanish" from the specified Player's view.
     * 
     * @param player is the Player to perform cloak effect on
     */
    private void toggleCloak( Player player )
    {

    }

    /**
     * Adds cloak item(s) to the specified inventory.
     * 
     * @param i Inventory where the cloak will be added.
     */
    public void giveCloak( Inventory i )
    {
        Material cloakMaterial = Material.getMaterial( getConfig().getString( "CLOAK_MATERIAL_TYPE" ) );
        if ( !i.contains( cloakMaterial ) )
        {
            i.addItem( new ItemStack( cloakMaterial, 1 ) );
        }
        else if ( !i.contains( cloakMaterial, 1 ) )
        {
            i.remove( cloakMaterial );
            i.addItem( new ItemStack( cloakMaterial, 1 ) );
        }
    }

    /**
     * Removes all existing potion effects at method call and adds potions effects as specified in the config.yml.
     * 
     * @param player The Player to add/remove potions effects to/from
     */
    private void handlePotions( Player player )
    {

        for ( PotionEffect effect : player.getActivePotionEffects() )
        {
            player.removePotionEffect( effect.getType() );
        }
        if ( getConfig().getBoolean( "Jump" ) && player.hasPermission( "bl.potjump" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.JUMP, getConfig().getInt( "JumpTime" ),
                                                      getConfig().getInt( "JumpHeight" ) ) );
        }
        if ( getConfig().getBoolean( "Speed" ) && player.hasPermission( "bl.potspeed" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.SPEED, getConfig().getInt( "SpeedTime" ),
                                                      getConfig().getInt( "SpeedLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Confusion" ) && player.hasPermission( "bl.potconfusion" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.CONFUSION,
                                                      getConfig().getInt( "ConfusionTime" ),
                                                      getConfig().getInt( "ConfusionLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Wither" ) && player.hasPermission( "bl.potwither" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.WITHER, getConfig().getInt( "WitherTime" ),
                                                      getConfig().getInt( "WitherLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Blindness" ) && player.hasPermission( "bl.potblindness" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.BLINDNESS,
                                                      getConfig().getInt( "BlindnessTime" ),
                                                      getConfig().getInt( "BlindnessLevel" ) ) );
        }
        if ( getConfig().getBoolean( "DamageResistance" ) && player.hasPermission( "bl.potdamage" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.DAMAGE_RESISTANCE,
                                                      getConfig().getInt( "DamageResistanceTime" ),
                                                      getConfig().getInt( "DamageResistanceLevel" ) ) );
        }
        if ( getConfig().getBoolean( "FastDigging" ) && player.hasPermission( "bl.potfastdigging" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.FAST_DIGGING,
                                                      getConfig().getInt( "FastDiggingTime" ),
                                                      getConfig().getInt( "FastDiggingLevel" ) ) );
        }
        if ( getConfig().getBoolean( "FireResitance" ) && player.hasPermission( "bl.potfireresistance" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.FIRE_RESISTANCE,
                                                      getConfig().getInt( "FireResitanceTime" ),
                                                      getConfig().getInt( "FireResitanceLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Harm" ) && player.hasPermission( "bl.potharm" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.HARM, getConfig().getInt( "HarmTime" ),
                                                      getConfig().getInt( "HarmLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Heal" ) && player.hasPermission( "bl.potheal" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.HEAL, getConfig().getInt( "HealTime" ),
                                                      getConfig().getInt( "HealLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Hunger" ) && player.hasPermission( "bl.pothunger" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.HUNGER, getConfig().getInt( "HungerTime" ),
                                                      getConfig().getInt( "HungerLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Strength" ) && player.hasPermission( "bl.potstrength" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.INCREASE_DAMAGE,
                                                      getConfig().getInt( "StrengthTime" ),
                                                      getConfig().getInt( "StrengthLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Invisibility" ) && player.hasPermission( "bl.potinvisible" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.INVISIBILITY,
                                                      getConfig().getInt( "InvisibilityTime" ),
                                                      getConfig().getInt( "InvisibilityLevel" ) ) );
        }
        if ( getConfig().getBoolean( "NightVision" ) && player.hasPermission( "bl.potnightvision" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.NIGHT_VISION,
                                                      getConfig().getInt( "NightVisionTime" ),
                                                      getConfig().getInt( "NightVisionLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Poison" ) && player.hasPermission( "bl.potpoison" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.POISON, getConfig().getInt( "PoisonTime" ),
                                                      getConfig().getInt( "PoisonLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Regeneration" ) && player.hasPermission( "bl.potregeneration" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.REGENERATION,
                                                      getConfig().getInt( "RegenerationTime" ),
                                                      getConfig().getInt( "RegenerationLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Slowness" ) && player.hasPermission( "bl.potslowness" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.SLOW, getConfig().getInt( "SlownessTime" ),
                                                      getConfig().getInt( "SlownessLevel" ) ) );
        }
        if ( getConfig().getBoolean( "SlowDigging" ) && player.hasPermission( "bl.potslowdigging" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.SLOW_DIGGING,
                                                      getConfig().getInt( "SlowDiggingTime" ),
                                                      getConfig().getInt( "SlowDiggingLevel" ) ) );
        }
        if ( getConfig().getBoolean( "WaterBreathing" ) && player.hasPermission( "bl.potwaterbreathing" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.WATER_BREATHING,
                                                      getConfig().getInt( "WaterBreathingTime" ),
                                                      getConfig().getInt( "WaterBreathingLevel" ) ) );
        }
        if ( getConfig().getBoolean( "Weakness" ) && player.hasPermission( "bl.potweakness" ) )
        {
            player.addPotionEffect( new PotionEffect( PotionEffectType.WEAKNESS, getConfig().getInt( "WeaknessTime" ),
                                                      getConfig().getInt( "WeaknessLevel" ) ) );
        }
    }
}