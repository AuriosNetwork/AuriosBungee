package net.aurios.auriosbungee.api;

import java.sql.SQLException;

import net.aurios.auriosbungee.AuriosBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

public class PunishmentAPI {
	
	public enum PunishmentType {
		WARN, MUTE, KICK, TEMPBAN, BAN;
	}
	
	AuriosBungee core;
	public PunishmentAPI(AuriosBungee core) {
		this.core = core;
	}
	
	//TODO: Find a better way of sending messages globally or to the specified players.
	
	public void punish(CommandSender cs, PunishmentType punishmentType, String offenderUUID, int length, String lengthUnit, String reason) {
		if(cs != null) {
			String uuid = "", server = "", displayName = "";
			if(cs instanceof ConsoleCommandSender) {
				uuid = "CONSOLE";
				server = "";
				displayName = "§b§lCONSOLE";
			} else if(cs instanceof ProxiedPlayer) {
				uuid = ((ProxiedPlayer)cs).getUniqueId().toString();
				server = ((ProxiedPlayer)cs).getServer().getInfo().getName();
				displayName = ((ProxiedPlayer)cs).getDisplayName();
			}
			if(punishmentType == PunishmentType.WARN) {
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.punishments.punish.warn")) {
					String playerName = (String) core.getMySQL().get("Username", "playerdata", "UUID", offenderUUID);
					if(playerName != null && !playerName.isEmpty()) {
						if(reason != null && !reason.isEmpty()) {
							core.getMySQL().update("INSERT INTO punishmentdata(ID, Staff, Punished, PunishmentType, Reason, Date, ExpirationDate, Appealed, AppealedBy) VALUES ('0', '" + uuid + "', '" + offenderUUID + "', 'WARN', '" + reason + "', NOW(), NULL, '0', NULL);");
							savePunishmentIDToPlayerData(offenderUUID);
							cs.sendMessage(new TextComponent(core.getPrefix() + "§eSuccessfully warned the player."));
							for(ProxiedPlayer pp : core.getProxy().getPlayers()) {
								if(server == null || server.isEmpty()) {
									//Sends a message globally if the punishment was issued by a console.
									pp.sendMessage(new TextComponent("§7----------[§a§lPunishment§7]----------"));
									pp.sendMessage(new TextComponent("§6§lIssuer: §r" + displayName));
									pp.sendMessage(new TextComponent("§6§lType: §7WARN"));
									pp.sendMessage(new TextComponent("§6§lPunished player: §r" + pp.getDisplayName()));
									pp.sendMessage(new TextComponent("§6§lReason: §7" + reason));
									pp.sendMessage(new TextComponent("§7--------------------------------"));
								}else if(pp.getServer().getInfo().getName().equals(server)) {
									//Sends a message to the players that were on the same server as the staff member, who issued the punishment.
									pp.sendMessage(new TextComponent("§7----------[§a§lPunishment§7]----------"));
									pp.sendMessage(new TextComponent("§6§lIssuer: §r" + displayName));
									pp.sendMessage(new TextComponent("§6§lType: §7WARN"));
									pp.sendMessage(new TextComponent("§6§lPunished player: §r" + pp.getDisplayName()));
									pp.sendMessage(new TextComponent("§6§lReason: §7" + reason));
									pp.sendMessage(new TextComponent("§7--------------------------------"));
								}
								//Display a title to the player.
								if(pp.getUniqueId().toString().equals(offenderUUID)) {
									Title t = core.getProxy().createTitle();
									t.title(new TextComponent("§c§lWARN")).subTitle(new TextComponent("§7" + reason)).fadeIn(20).stay(60).fadeOut(20);
									t.send(pp);
								}
							}
						}else{
							cs.sendMessage(new TextComponent(core.getPrefix() + "§cYou must specify a reason."));
						}
					}else{
						cs.sendMessage(new TextComponent(core.getPrefix() + "§cIt seems like that player has never joined §cour network."));
					}
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else if(punishmentType == PunishmentType.MUTE){
				if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.punishments.punish.mute")) {
					String playerName = (String) core.getMySQL().get("Username", "playerdata", "UUID", offenderUUID);
					if(playerName != null && !playerName.isEmpty()) {
						if(reason != null && !reason.isEmpty()) {
							core.getMySQL().update("INSERT INTO punishmentdata(ID, Staff, Punished, PunishmentType, Reason, Date, ExpirationDate, Appealed, AppealedBy) VALUES ('0', '" + uuid + "', '" + offenderUUID + "', 'MUTE', '" + reason + "', NOW(), NULL, '0', NULL);");
							savePunishmentIDToPlayerData(offenderUUID);
							cs.sendMessage(new TextComponent(core.getPrefix() + "§eSuccessfully warned the player."));
							for(ProxiedPlayer pp : core.getProxy().getPlayers()) {
								if(server == null || server.isEmpty()) {
									//Sends a message globally if the punishment was issued by a console.
									pp.sendMessage(new TextComponent("§7----------[§a§lPunishment§7]----------"));
									pp.sendMessage(new TextComponent("§6§lIssuer: §r" + displayName));
									pp.sendMessage(new TextComponent("§6§lType: §7WARN"));
									pp.sendMessage(new TextComponent("§6§lPunished player: §r" + pp.getDisplayName()));
									pp.sendMessage(new TextComponent("§6§lReason: §7" + reason));
									pp.sendMessage(new TextComponent("§7--------------------------------"));
								}else if(pp.getServer().getInfo().getName().equals(server)) {
									//Sends a message to the players that were on the same server as the staff member, who issued the punishment.
									pp.sendMessage(new TextComponent("§7----------[§a§lPunishment§7]----------"));
									pp.sendMessage(new TextComponent("§6§lIssuer: §r" + displayName));
									pp.sendMessage(new TextComponent("§6§lType: §7WARN"));
									pp.sendMessage(new TextComponent("§6§lPunished player: §r" + pp.getDisplayName()));
									pp.sendMessage(new TextComponent("§6§lReason: §7" + reason));
									pp.sendMessage(new TextComponent("§7--------------------------------"));
								}
								//Display a title to the player.
								if(pp.getUniqueId().toString().equals(offenderUUID)) {
									Title t = core.getProxy().createTitle();
									t.title(new TextComponent("§c§lWARN")).subTitle(new TextComponent("§7" + reason)).fadeIn(20).stay(60).fadeOut(20);
									t.send(pp);
								}
							}
						}else{
							cs.sendMessage(new TextComponent(core.getPrefix() + "§cYou must specify a reason."));
						}
					}else{
						cs.sendMessage(new TextComponent(core.getPrefix() + "§cIt seems like that player has never joined §cour network."));
					}
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else if(punishmentType == PunishmentType.KICK){
				
			}else if(punishmentType == PunishmentType.TEMPBAN){
				
			}else if(punishmentType == PunishmentType.BAN){
				
			}
		}
	}
	
	private void savePunishmentIDToPlayerData(String uuid) {
		int id = 0;
		try {
			id = core.getMySQL().countRows("punishmentdata");
			core.getMySQL().set("playerdata", "LastPunishment", id, "UUID", uuid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void sendHelp(CommandSender cs) {
		cs.sendMessage(new TextComponent("§7§m-----------------=§b§m[§r §6§lPUNISHMENTS §b§m]§7§m=-----------------"));
		cs.sendMessage(new TextComponent("§e— §a/warn <player> <reason> §e - §7Warns a player."));
		cs.sendMessage(new TextComponent("§e— §a/mute <player> <length> <unit> <reason> §e - §7Mutes a player."));
		cs.sendMessage(new TextComponent("§e— §a/kick <player> <reason> §e - §7Adds player to the §7group."));
		cs.sendMessage(new TextComponent("§e— §a/tb <player> <length> <unit> <reason>§e - §7Temporarily bans a player."));
		cs.sendMessage(new TextComponent("§e— §a/ban <player> <reason> §e - §7Permanently bans a player."));
		cs.sendMessage(new TextComponent("§e— §a/appeal <punishmentID> §e - §7Appeals a punishment."));
	}

}
