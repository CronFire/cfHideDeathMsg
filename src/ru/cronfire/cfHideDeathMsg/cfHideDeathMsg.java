package ru.cronfire.cfHideDeathMsg;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Enabled cfHideDeathMsg v" + getDescription().getVersion());
	}

	public void onDisable() {
		getLogger().info("Disabled cfHideDeathMsg v" + getDescription().getVersion());
	}

	@SuppressWarnings("unused")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDeathEvent(final PlayerDeathEvent event) {
		if(!event.getEntity().hasPermission("cfhidedeathmsg.exempt")) {
			event.setDeathMessage(null);
		}
	}
}