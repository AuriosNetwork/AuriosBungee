package net.aurios.auriosbungee.commands;

import net.aurios.auriosbungee.AuriosBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Group extends Command {

	AuriosBungee core;
	public Group(AuriosBungee core) {
		super("group");
		this.core = core;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if(args.length < 1 || args.length > 3) { 
			core.getGroupsAPI().sendHelp(cs);
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("create")) {
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
				cs.sendMessage(new TextComponent("§e● §a/group create <name> <value> §e - §7Creates a new group with §7a given value."));
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
			}else if(args[0].equalsIgnoreCase("remove")) {
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
				cs.sendMessage(new TextComponent("§e● §a/group remove <name> §e - §7Completely removes the existing §7group."));
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
			}else if(args[0].equalsIgnoreCase("addplayer")) {
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
				cs.sendMessage(new TextComponent("§e● §a/group addplayer <name> <player> §e - §7Adds player to the §7group."));
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
			}else if(args[0].equalsIgnoreCase("removeplayer")) {
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
				cs.sendMessage(new TextComponent("§e● §a/group removeplayer <name> <player> §e - §7Removes player §7from the group."));
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
			}else if(args[0].equalsIgnoreCase("addperm")) {
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
				cs.sendMessage(new TextComponent("§e● §a/group addperm <name> <permission> §e - §7Adds permission to §7the group."));
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
			}else if(args[0].equalsIgnoreCase("remperm")) {
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
				cs.sendMessage(new TextComponent("§e● §a/group remperm <name> <permission> §e - §7Removes permission §7from the group."));
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
			}else if(args[0].equalsIgnoreCase("setprefix")) {
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
				cs.sendMessage(new TextComponent("§e● §a/group setprefix <name> <prefix> §e - §7Sets group's prefix."));
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
			}else if(args[0].equalsIgnoreCase("setchatcolor")) {
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
				cs.sendMessage(new TextComponent("§e● §a/group setchatcolor <name> <color> §e - §7Sets group's chat color."));
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
			}else if(args[0].equalsIgnoreCase("setvalue")) {
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
				cs.sendMessage(new TextComponent("§e● §a/group setvalue <name> <value> §e - §7Sets group's value."));
				cs.sendMessage(new TextComponent("§7----------------------------------------"));
			}else{
				cs.sendMessage(new TextComponent(core.getPrefix() + "§cUnknown sub-command. Please use /group §cto display detailed help."));
			}
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("remove")) {
				String name = args[1];
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.groups.group.remove")) {
					core.getGroupsAPI().removeGroup(cs, name);
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else{
				core.getGroupsAPI().sendHelp(cs);
			}
		}else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("create")) {
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.groups.group.create")) {
					String name = args[1];
					int value = 0;
					try{
						value = Integer.parseInt(args[2]);
						core.getGroupsAPI().createNewGroup(cs, name, value);
					}catch(NumberFormatException ex) {
						cs.sendMessage(new TextComponent(core.getPrefix() + "§cValue must be a number (1-99). Higher §crank means higher value!"));
					}
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else if(args[0].equalsIgnoreCase("addplayer")) {
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.groups.group.addplayer")) {
					String name = args[1];
					String playerName = args[2];
					String uuid = (String) core.getMySQL().get("UUID", "playerdata", "Username", playerName);
					if(uuid != null && !uuid.isEmpty()) {
						core.getGroupsAPI().addPlayerToGroup(cs, name, uuid);
					}else{
						cs.sendMessage(new TextComponent(core.getPrefix() + "§cIt seems like that player has never joined §cour network."));
					}
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else if(args[0].equalsIgnoreCase("removeplayer")) {
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.groups.group.removeplayer")) {
					String name = args[1];
					String playerName = args[2];
					String uuid = (String) core.getMySQL().get("UUID", "playerdata", "Username", playerName);
					if(uuid != null && !uuid.isEmpty()) {
						core.getGroupsAPI().removePlayerFromGroup(cs, name, uuid);
					}else{
						cs.sendMessage(new TextComponent(core.getPrefix() + "§cIt seems like that player has never joined §cour network."));
					}
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else if(args[0].equalsIgnoreCase("addperm")) {
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.groups.group.addperm")) {
					String group = args[1];
					String perm = args[2];
					core.getGroupsAPI().addPermissionToTheGroup(cs, group, perm);
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else if(args[0].equalsIgnoreCase("remperm")) {
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.groups.group.remperm")) {
					String group = args[1];
					String perm = args[2];
					core.getGroupsAPI().removePermissionFromTheGroup(cs, group, perm);
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else if(args[0].equalsIgnoreCase("setprefix")) {
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.groups.group.setprefix")) {
					String group = args[1];
					String prefix = args[2];
					core.getGroupsAPI().setPrefix(cs, group, prefix);
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else if(args[0].equalsIgnoreCase("setchatcolor")) {
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.groups.group.setchatcolor")) {
					String group = args[1];
					String chatcolor = args[2];
					core.getGroupsAPI().setPrefix(cs, group, chatcolor);
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else if(args[0].equalsIgnoreCase("setvalue")) {
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.groups.group.setvalue")) {
					String group = args[1];
					int value = 1;
					try{
						value = Integer.parseInt(args[2]);
						core.getGroupsAPI().setValue(cs, group, value);
					}catch(NumberFormatException ex) {
						cs.sendMessage(new TextComponent(core.getPrefix() + "§cValue must be a number (1-99). Higher §crank means higher value!"));
						ex.printStackTrace();
					}
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else{
				core.getGroupsAPI().sendHelp(cs);
			}
		}
	}
	
}
