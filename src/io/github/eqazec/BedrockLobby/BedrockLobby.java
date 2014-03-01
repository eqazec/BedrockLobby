package io.github.eqazec.BedrockLobby;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * 
 * BedrockLobbyPlugin Class
 * @author eqazec
 *
 */
public final class BedrockLobby extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);	
		this.saveDefaultConfig();
		getLogger().info("Enabled!");
	}
	
	@Override
	public void onDisable(){
		getLogger().info("Disabled!");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		//Clears player inventory on join if that mechanic is enabled and the player is not exempt.
		if(getConfig().getBoolean("ClearInventoryOnJoin") && !(player.hasPermission("bl.invclearexempt")))
			player.getInventory().clear();
		//If JoinPotions is enabled, calls HandlePotions(player).
		if(getConfig().getBoolean("JoinPotions"))
			HandlePotions(player);
		//If GivePlayerCloak is true, gives the player a cloak item.
		if(getConfig().getBoolean("GivePlayerCloak")){
			player.getInventory().addItem(MATERIAL.getConfig().getString("CloakItem"), 1);
		}
	}
	
	private void HandlePotions(Player player) {
		if ((getConfig().getBoolean("RemovePotions")) && (player.hasPermission("bl.removepots")))
		    for (PotionEffect effect : player.getActivePotionEffects())
		    	player.removePotionEffect(effect.getType());
		if ((getConfig().getBoolean("Jump")) && (player.hasPermission("bl.potjump")))
		    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, getConfig().getInt("JumpTime"), getConfig().getInt("JumpHeight")));
		if ((getConfig().getBoolean("Speed")) && (player.hasPermission("bl.potspeed")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, getConfig().getInt("SpeedTime"), getConfig().getInt("SpeedLevel")));
		if ((getConfig().getBoolean("Confusion")) && (player.hasPermission("bl.potconfusion")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, getConfig().getInt("ConfusionTime"), getConfig().getInt("ConfusionLevel")));
		if ((getConfig().getBoolean("Wither")) && (player.hasPermission("bl.potwither")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, getConfig().getInt("WitherTime"), getConfig().getInt("WitherLevel")));
		if ((getConfig().getBoolean("Blindness")) && (player.hasPermission("bl.potblindness")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, getConfig().getInt("BlindnessTime"), getConfig().getInt("BlindnessLevel")));
		if ((getConfig().getBoolean("DamageResistance")) && (player.hasPermission("bl.potdamage")))
	    	player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, getConfig().getInt("DamageResistanceTime"), getConfig().getInt("DamageResistanceLevel")));
		if ((getConfig().getBoolean("FastDigging")) && (player.hasPermission("bl.potfastdigging")))
	    	player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, getConfig().getInt("FastDiggingTime"), getConfig().getInt("FastDiggingLevel")));
		if ((getConfig().getBoolean("FireResitance")) && (player.hasPermission("bl.potfireresistance")))
			player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, getConfig().getInt("FireResitanceTime"), getConfig().getInt("FireResitanceLevel")));
		if ((getConfig().getBoolean("Harm")) && (player.hasPermission("bl.potharm")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, getConfig().getInt("HarmTime"), getConfig().getInt("HarmLevel")));
		if ((getConfig().getBoolean("Heal")) && (player.hasPermission("bl.potheal")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, getConfig().getInt("HealTime"), getConfig().getInt("HealLevel")));
		if ((getConfig().getBoolean("Hunger")) && (player.hasPermission("bl.pothunger")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, getConfig().getInt("HungerTime"), getConfig().getInt("HungerLevel")));
		if ((getConfig().getBoolean("Strength")) && (player.hasPermission("bl.potstrength")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, getConfig().getInt("StrengthTime"), getConfig().getInt("StrengthLevel")));
		if ((getConfig().getBoolean("Invisibility")) && (player.hasPermission("bl.potinvisible")))
	    	player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, getConfig().getInt("InvisibilityTime"), getConfig().getInt("InvisibilityLevel")));
		if ((getConfig().getBoolean("NightVision")) && (player.hasPermission("bl.potnightvision")))
		    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, getConfig().getInt("NightVisionTime"), getConfig().getInt("NightVisionLevel")));
		if ((getConfig().getBoolean("Poison")) && (player.hasPermission("bl.potpoison")))
		    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, getConfig().getInt("PoisonTime"), getConfig().getInt("PoisonLevel")));
	    if ((getConfig().getBoolean("Regeneration")) && (player.hasPermission("bl.potregeneration")))
	    	player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, getConfig().getInt("RegenerationTime"), getConfig().getInt("RegenerationLevel")));
	    if ((getConfig().getBoolean("Slowness")) && (player.hasPermission("bl.potslowness")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, getConfig().getInt("SlownessTime"), getConfig().getInt("SlownessLevel")));
	    if ((getConfig().getBoolean("SlowDigging")) && (player.hasPermission("bl.potslowdigging")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, getConfig().getInt("SlowDiggingTime"), getConfig().getInt("SlowDiggingLevel")));
	    if ((getConfig().getBoolean("WaterBreathing")) && (player.hasPermission("bl.potwaterbreathing")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, getConfig().getInt("WaterBreathingTime"), getConfig().getInt("WaterBreathingLevel")));
	    if ((getConfig().getBoolean("Weakness")) && (player.hasPermission("bl.potweakness")))
		   	player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, getConfig().getInt("WeaknessTime"), getConfig().getInt("WeaknessLevel")));
	}
}