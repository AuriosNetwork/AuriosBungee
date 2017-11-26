package net.aurios.auriosbungee.commands;

import net.aurios.auriosbungee.AuriosBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Warn extends Command {
	
	AuriosBungee core;
	public Warn(AuriosBungee core) {
		super("warn");
		this.core = core;
	}

	@Override
	public void execute(CommandSender c, String[] args) {
		if(args.length < 2) {
			core.getPunishmentAPI().sendHelp(c);
		}else{
			String playerName = args[0];
			String reason = "";
			for(int i = 1; i < args.length; i++) {
				reason += args[i] + " ";
			}
			reason = reason.substring(0, 1).toUpperCase() + reason.substring(1).toLowerCase();
			core.getPunishmentAPI().warn(c, playerName, reason);
		}
	}

}
