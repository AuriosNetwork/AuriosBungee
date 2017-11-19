package net.aurios.auriosbungee.commands;

import net.aurios.auriosbungee.AuriosBungee;
import net.aurios.auriosbungee.api.PunishmentAPI.PunishmentType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Mute extends Command {

	AuriosBungee core;
	public Mute(AuriosBungee core) {
		super("mute");
		this.core = core;
	}

	@Override
	public void execute(CommandSender c, String[] args) {
		if(args.length < 2) {
			core.getPunishmentAPI().sendHelp(c);
		}else{
			String playerName = args[0];
			ProxiedPlayer target = core.getProxy().getPlayer(playerName);
			if(target != null) {
				String uuid = target.getUniqueId().toString();
				String reason = "";
				for(int i = 1; i < args.length; i++) {
					reason += args[i] + " ";
				}
				reason = reason.substring(0, 1).toUpperCase() + reason.substring(1).toLowerCase();
				core.getPunishmentAPI().punish(c, PunishmentType.MUTE, uuid, 0, null, reason);
			}else{
				c.sendMessage(new TextComponent(core.getPrefix() + "§cThis player is not online."));
			}
		}
	}

}
