package ru.cronfire.cfhidedeathmsg;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

/*
 * COPYRIGHT (c) 2012 Zeluboba (Roman Zabaluev)
 * This file is part of cfHideDeathMsg
 * Package: ru.cronfire.cfhidedeathmsg
 * Date: 27.05.2012
 * Time: 00:00
 * DO NOT DISTRIBUTE.
 */

@SuppressWarnings("UnusedDeclaration")
public class CronFireHideDeathMesage extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		loadConfig();
		reloadConfig();

		getServer().getPluginManager().registerEvents(this, this);

		getLogger().info("Enabled cfHideDeathMsg v" + getDescription().getVersion());
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled cfHideDeathMsg v" + getDescription().getVersion());
	}

	@SuppressWarnings("WeakerAccess")
	public void loadConfig() {
		getConfig().addDefault("radius.enable", "false");
		getConfig().addDefault("radius.radius", "200");

		getConfig().options().copyDefaults(true);
		saveConfig();
		getLogger().info("Successfully loaded configuration file.");
	}

	@SuppressWarnings("unused")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDeathEvent(final PlayerDeathEvent event) {
		if(!event.getEntity().hasPermission("cfhidedeathmsg.exempt")) {
			event.setDeathMessage(null);
			return;
		}

		if(getConfig().getBoolean("radius.enable")) {
			final int radius = getConfig().getInt("radius.radius");
			Location location = event.getEntity().getLocation();
			for(Player player : getPlayersInRange(location, radius)) {
				if(player != null) {
					if(player.hasPermission("cfhidedeathmsg.recieve")) {
						player.sendMessage(event.getDeathMessage());
					}
				}
			}
		}
	}

	private List<Player> getPlayersInRange(Location loc, double radius) {
		List<Player> players = new LinkedList<Player>();
		for(Player player : loc.getWorld().getPlayers())
			if(player.getLocation().distanceSquared(loc) <= Math.pow(radius, 2))
				players.add(player);
		return players;
	}

}