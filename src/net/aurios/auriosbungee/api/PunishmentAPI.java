package net.aurios.auriosbungee.api;

import java.sql.SQLException;
import java.util.UUID;

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
	
	public void punish(CommandSender cs, PunishmentType punishmentType, String offenderUUID, int length, String lengthUnit, String reason) {
		if(cs != null) {
			String uuid = "", server = "", displayName = "";
			if(cs instanceof ConsoleCommandSender) {
				uuid = "CONSOLE";
				server = "";
				displayName = "§b§lCONSOLE§r ";
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
							for(ProxiedPlayer pp : core.getProxy().getPlayers()) {
								if(pp.getServer().getInfo().getName().equalsIgnoreCase(server)) {
									pp.sendMessage(new TextComponent(displayName + " §7warned " + core.getProxy().getPlayer(UUID.fromString(offenderUUID)).getDisplayName() + " §7for: §6" + reason));
								}
								if(pp.getUniqueId().toString().equals(offenderUUID)) {
									ProxiedPlayer o = core.getProxy().getPlayer(UUID.fromString(offenderUUID));//Offender
									Title t = core.getProxy().createTitle();
									t.title(new TextComponent("§c§lWARN")).subTitle(new TextComponent("§7" + reason)).fadeIn(20).stay(60).fadeOut(20);
									t.send(o);
									
								}
							}
						}
					}else{
						cs.sendMessage(new TextComponent(core.getPrefix() + "§cIt seems like that player has never joined §cour network."));
					}
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
				}
			}else if(punishmentType == PunishmentType.MUTE){
				
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
		cs.sendMessage(new TextComponent("§7§m-------------------=§b§m[§r §6§lPUNISHMENTS §b§m]§7§m=-------------------"));
		cs.sendMessage(new TextComponent("§e● §a/warn <player> <reason> §e - §7Warns a player."));
		cs.sendMessage(new TextComponent("§e● §a/mute <player> <length> <unit> <reason> §e - §7Mutes a player."));
		cs.sendMessage(new TextComponent("§e● §a/kick <player> <reason> §e - §7Adds player to the §7group."));
		cs.sendMessage(new TextComponent("§e● §a/tb <player> <length> <unit> <reason>§e - §7Temporarily bans a player."));
		cs.sendMessage(new TextComponent("§e● §a/ban <player> <reason> §e - §7Permanently bans a player."));
		cs.sendMessage(new TextComponent("§e● §a/appeal <punishmentID> §e - §7Appeals a punishment."));
	}

}
