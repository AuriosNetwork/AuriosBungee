package net.aurios.auriosbungee.commands;

import net.aurios.auriosbungee.AuriosBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
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
			int length = 0;
			try{
				length = Integer.parseInt(args[1]);
			}catch(NumberFormatException ex) {
				ex.printStackTrace();
				c.sendMessage(new TextComponent(core.getPrefix() + "§cLength should be a number!"));
				return;
			}
			String reason = "";
			for(int i = 3; i < args.length; i++) {
				reason += args[i] + " ";
			}
			reason = reason.substring(0, 1).toUpperCase() + reason.substring(1).toLowerCase();
			core.getPunishmentAPI().mute(c, playerName, length, args[2], reason);
		}
	}

}
