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
	}

}
