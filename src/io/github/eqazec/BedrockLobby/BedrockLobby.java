/*
 * BedrockLobby - a custom bukkit plugin
 */
package io.github.eqazec.bedrocklobby;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class BedrockLobby extends JavaPlugin implements Listener
{
    public static final int              BUKKIT_VERSION = 2922;
    private static final Logger          LOGGER         = Logger.getLogger("BedrockLobby");
    private transient Backup             backup;
    private transient PermissionsHandler permissionsHandler;
    private transient List<String>       cloakedPlayers = new ArrayList<String>();
    private transient List<String>       cloakSpammers  = new ArrayList<String>();

    public BedrockLobby()
    {

    }

    public BedrockLobby(final Server server)
    {
        super(new JavaPluginLoader(server), new PluginDescriptionFile("BedrockLobby", "", "io.github.eqazec.bedrocklobby.BedrockLobby"), null, null);
    }

    @Override
    public void onEnable()
    {
        try 
        {
            LOGGER.setParent(this.getLogger());
            final PluginManager pm = getServer().getPluginManager();
            for(Plugin plugin : pm.getPlugins())
            {
                if(plugin.getDescription().getName().startsWith("BedrockLobby")
                        && !plugin.getDescription().getVersion().equals(this.getDescription().getVersion())
                        && !plugin.getDescription().getName().equals("BedrockLobby"))
                {
                    LOGGER.log(Level.WARNING, _("versionMismatch", plugin.getDescription().getName()));
                }
            }
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable()
    {
        saveConfig();
        getLogger().info("Disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {

        Player player = event.getPlayer();

        Inventory inv = player.getInventory();

        // Clears player inventory on join if that mechanic is enabled and the
        // player is not exempt.
        if (getConfig().getBoolean("ClearInventoryOnJoin") && !player.hasPermission("bl.invclearexempt"))
        {
            inv.clear();
        }

        // If enabled, give players join potions per configured settings
        if (getConfig().getBoolean("JoinPotions"))
        {
            handlePotions(player);
        }

        // If GivePlayerCloak is true, gives the player a cloak item.
        if (getConfig().getBoolean("EnableCloak"))
        {
            if (!inv.contains(cloakItem))
            {
                inv.addItem(new ItemStack(cloakItem, 1));
            } else if (!inv.contains(cloakItem, 1))
            {
                inv.remove(cloakItem);
                inv.addItem(new ItemStack(cloakItem, 1));
            }
        }
    }

    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent e)
    {
        cloakToggled.remove(e.getPlayer());
        cloakSpam.remove(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        Player player = event.getPlayer();
        if (((e.getAction().equals(Action.RIGHT_CLICK_AIR)) || (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
                && (player.getInventory().getItemInHand().getType() == cloakItem))
        {
            toggleCloak(player);
        }
    }

    private void toggleCloak(Player player)
    {

    }

    private void handlePotions(Player player)
    {

        for (PotionEffect effect : player.getActivePotionEffects())
        {
            player.removePotionEffect(effect.getType());
        }
        if (getConfig().getBoolean("Jump") && player.hasPermission("bl.potjump"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, getConfig().getInt("JumpTime"), getConfig().getInt("JumpHeight")));
        }
        if (getConfig().getBoolean("Speed") && player.hasPermission("bl.potspeed"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, getConfig().getInt("SpeedTime"), getConfig().getInt("SpeedLevel")));
        }
        if (getConfig().getBoolean("Confusion") && player.hasPermission("bl.potconfusion"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, getConfig().getInt("ConfusionTime"), getConfig().getInt("ConfusionLevel")));
        }
        if (getConfig().getBoolean("Wither") && player.hasPermission("bl.potwither"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, getConfig().getInt("WitherTime"), getConfig().getInt("WitherLevel")));
        }
        if (getConfig().getBoolean("Blindness") && player.hasPermission("bl.potblindness"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, getConfig().getInt("BlindnessTime"), getConfig().getInt("BlindnessLevel")));
        }
        if (getConfig().getBoolean("DamageResistance") && player.hasPermission("bl.potdamage"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, getConfig().getInt("DamageResistanceTime"), getConfig().getInt("DamageResistanceLevel")));
        }
        if (getConfig().getBoolean("FastDigging") && player.hasPermission("bl.potfastdigging"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, getConfig().getInt("FastDiggingTime"), getConfig().getInt("FastDiggingLevel")));
        }
        if (getConfig().getBoolean("FireResitance") && player.hasPermission("bl.potfireresistance"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, getConfig().getInt("FireResitanceTime"), getConfig().getInt("FireResitanceLevel")));
        }
        if (getConfig().getBoolean("Harm") && player.hasPermission("bl.potharm"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, getConfig().getInt("HarmTime"), getConfig().getInt("HarmLevel")));
        }
        if (getConfig().getBoolean("Heal") && player.hasPermission("bl.potheal"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, getConfig().getInt("HealTime"), getConfig().getInt("HealLevel")));
        }
        if (getConfig().getBoolean("Hunger") && player.hasPermission("bl.pothunger"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, getConfig().getInt("HungerTime"), getConfig().getInt("HungerLevel")));
        }
        if (getConfig().getBoolean("Strength") && player.hasPermission("bl.potstrength"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, getConfig().getInt("StrengthTime"), getConfig().getInt("StrengthLevel")));
        }
        if (getConfig().getBoolean("Invisibility") && player.hasPermission("bl.potinvisible"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, getConfig().getInt("InvisibilityTime"), getConfig().getInt("InvisibilityLevel")));
        }
        if (getConfig().getBoolean("NightVision") && player.hasPermission("bl.potnightvision"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, getConfig().getInt("NightVisionTime"), getConfig().getInt("NightVisionLevel")));
        }
        if (getConfig().getBoolean("Poison") && player.hasPermission("bl.potpoison"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, getConfig().getInt("PoisonTime"), getConfig().getInt("PoisonLevel")));
        }
        if (getConfig().getBoolean("Regeneration") && player.hasPermission("bl.potregeneration"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, getConfig().getInt("RegenerationTime"), getConfig().getInt("RegenerationLevel")));
        }
        if (getConfig().getBoolean("Slowness") && player.hasPermission("bl.potslowness"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, getConfig().getInt("SlownessTime"), getConfig().getInt("SlownessLevel")));
        }
        if (getConfig().getBoolean("SlowDigging") && player.hasPermission("bl.potslowdigging"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, getConfig().getInt("SlowDiggingTime"), getConfig().getInt("SlowDiggingLevel")));
        }
        if (getConfig().getBoolean("WaterBreathing") && player.hasPermission("bl.potwaterbreathing"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, getConfig().getInt("WaterBreathingTime"), getConfig().getInt("WaterBreathingLevel")));
        }
        if (getConfig().getBoolean("Weakness") && player.hasPermission("bl.potweakness"))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, getConfig().getInt("WeaknessTime"), getConfig().getInt("WeaknessLevel")));
        }
    }

    private static String colorize(String message)
    {
        return message.replaceAll("~([a-z0-9])", "§$1");
    }
}