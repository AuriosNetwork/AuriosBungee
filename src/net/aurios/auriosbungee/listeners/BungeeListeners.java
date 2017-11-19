package net.aurios.auriosbungee.listeners;

import net.aurios.auriosbungee.AuriosBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListeners implements Listener {
	
	AuriosBungee core;
	public BungeeListeners(AuriosBungee core) {
		this.core = core;
	}
	
	@EventHandler
	public void onPostLogin(PostLoginEvent e) {
		ProxiedPlayer pp = e.getPlayer();
		core.getPlayerDataAPI().registerPlayer(pp);
		String group = core.getGroupsAPI().getTheMostValuablePlayerGroup(core.getGroupsAPI().getPlayerGroups(pp.getUniqueId().toString()));
		String prefix = ChatColor.translateAlternateColorCodes('&', core.getGroupsAPI().getPrefix(group));
		pp.setDisplayName(prefix + " §7§o" + pp.getName());
	}

}
