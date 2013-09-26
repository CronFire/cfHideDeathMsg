package ru.cronfire.cfHideDeathMsg;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

/*
 * COPYRIGHT (c) 2012 Zeluboba (Roman Zabaluev)
 * This file is part of cfBlazeProtect
 * Package: ru.cronfire.cfBlazeProtect
 * Date: 27.05.2012
 * Time: 00:00
 * DO NOT DISTRIBUTE.
 */

public class cfHideDeathMsg extends JavaPlugin implements Listener {

	public void onEnable() {
		loadConfig();
		reloadConfig();

		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Enabled cfHideDeathMsg v" + getDescription().getVersion());
	}

	public void onDisable() {
		getLogger().info("Disabled cfHideDeathMsg v" + getDescription().getVersion());
	}

	public void loadConfig() {
		getConfig().addDefault("radius.use", "false");
		getConfig().addDefault("radius.radius", "200");

		getConfig().options().copyDefaults(true);
		saveConfig();
		getLogger().info("Successfully loaded configuration file.");
	}

	@SuppressWarnings("unused")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDeathEvent(final PlayerDeathEvent event) {
		if(event.getEntity().hasPermission("cfhidedeathmsg.exempt")) {
			if(getConfig().getBoolean("radius.use")) {
				int radius = getConfig().getInt("radius.radius");
				Location location = event.getEntity().getLocation();
				for(Entity entity : getNearbyEntities(location, radius)) {
					if(entity instanceof Player) {
						Player player = (Player) entity;
						if(player.hasPermission("cfhidedeathmsg.recieve")) {
							player.sendMessage(event.getDeathMessage());
						}
					}
				}
			}
		} else {
			event.setDeathMessage(null);
		}
	}

	public Entity[] getNearbyEntities(Location l, int radius) {
		int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
		HashSet<Entity> radiusEntities = new HashSet<Entity>();
		for(int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
			for(int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
				int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
				for(Entity e : new Location(l.getWorld(), x + (chX * 16), y, z
						+ (chZ * 16)).getChunk().getEntities()) {
					if(e.getLocation().distance(l) <= radius
							&& e.getLocation().getBlock() != l.getBlock()) {
						radiusEntities.add(e);
					}
				}
			}
		}
		return radiusEntities.toArray(new Entity[radiusEntities.size()]);
	}
}