package net.aurios.auriosbungee.api;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import net.aurios.auriosbungee.AuriosBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

public class PunishmentAPI {
	
	AuriosBungee core;
	public PunishmentAPI(AuriosBungee core) {
		this.core = core;
	}
	
	public void warn(CommandSender cs, String offenderUsername, String reason) {
		if(cs != null) {
			if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.punishments.punish.warn")) {
				String[] senderData = getSenderData(cs).split(";");
				String uuid = senderData[0];
				String server = senderData[1];
				String displayName = senderData[2];
				String offenderUUID = (String) core.getMySQL().get("UUID", "playerdata", "Username", offenderUsername);
				if(offenderUUID != null && !offenderUUID.isEmpty()) {
					if(reason != null && !reason.isEmpty()) {
						String odn = "";
						if(core.getProxy().getPlayer(offenderUsername) != null) odn = (String) core.getProxy().getPlayer(UUID.fromString(offenderUUID)).getDisplayName();
						else odn = "§7" + (String) core.getMySQL().get("Username", "playerdata", "UUID", offenderUUID);
						core.getMySQL().update("INSERT INTO punishmentdata(ID, Staff, Punished, PunishmentType, Reason, Date, ExpirationDate, Appealed, AppealedBy) VALUES ('0', '" + uuid + "', '" + offenderUUID + "', 'WARN', '" + reason + "', NOW(), NULL, '0', NULL);");
						savePunishmentIDToPlayerData(offenderUUID);
						if(server != null && !server.isEmpty()) {
							for(ProxiedPlayer pp : core.getProxy().getPlayers()) {
								if(pp.getServer().getInfo().getName().equals(server)) {
									pp.sendMessage(new TextComponent("§7---------[§a§lPunishment§7]---------"));
									pp.sendMessage(new TextComponent("§6§lIssuer: §r" + displayName));
									pp.sendMessage(new TextComponent("§6§lType: §7WARN"));
									pp.sendMessage(new TextComponent("§6§lPunished player: §r" + odn));
									pp.sendMessage(new TextComponent("§6§lReason: §7" + reason));
									pp.sendMessage(new TextComponent("§7------------------------------"));
								}
								if(pp.getUniqueId().toString().equals(offenderUUID)) {
									Title t = core.getProxy().createTitle();
									t.title(new TextComponent("§c§lWARN")).subTitle(new TextComponent("§7" + reason)).fadeIn(20).stay(60).fadeOut(20);
									t.send(pp);
								}
							}
						}else{
							cs.sendMessage(new TextComponent(core.getPrefix() + "§aSuccessfully punished a player!"));
						}
					}else{
						cs.sendMessage(new TextComponent(core.getPrefix() + "§cYou must specify a reason!"));
					}
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cLooks like this player has never played on §cour network."));
				}
				
			}else{
				cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
			}
		}
	}

	public void mute(CommandSender cs, String offenderUsername, int length, String lengthUnit, String reason) {
		if(cs != null) {
			if(core.getGroupsAPI().hasPermission(cs, "auriosbungee.punishments.punish.warn")) {
				String[] senderData = getSenderData(cs).split(";");
				String uuid = senderData[0];
				String server = senderData[1];
				String displayName = senderData[2];
				String offenderUUID = (String) core.getMySQL().get("UUID", "playerdata", "Username", offenderUsername);
				if(offenderUUID != null && !offenderUUID.isEmpty()) {
					if(length > 0 && (lengthUnit.startsWith("s") || lengthUnit.startsWith("min") || lengthUnit.startsWith("h") || lengthUnit.startsWith("d") || lengthUnit.startsWith("mo") || lengthUnit.startsWith("y"))) {
						if(reason != null && !reason.isEmpty()) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date now = new Date();
							Date expirationDate = null;
							try {
								expirationDate = core.getTimeUtils().addToDate(sdf.format(now), core.getTimeUtils().timeUnit(lengthUnit), length);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							if(expirationDate != null) {
								String odn = "";
								if(core.getProxy().getPlayer(offenderUsername) != null) odn = (String) core.getProxy().getPlayer(UUID.fromString(offenderUUID)).getDisplayName();
								else odn = "§7" + (String) core.getMySQL().get("Username", "playerdata", "UUID", offenderUUID);
								core.getMySQL().update("INSERT INTO punishmentdata(ID, Staff, Punished, PunishmentType, Reason, Date, ExpirationDate, Appealed, AppealedBy) VALUES ('0', '" + uuid + "', '" + offenderUUID + "', 'MUTE', '" + reason + "', NOW(), '" + sdf.format(expirationDate) + "', '0', NULL);");
								savePunishmentIDToPlayerData(offenderUUID);
								if(server != null && !server.isEmpty()) {
									for(ProxiedPlayer pp : core.getProxy().getPlayers()) {
										if(pp.getServer().getInfo().getName().equals(server)) {
											pp.sendMessage(new TextComponent("§7---------[§a§lPunishment§7]---------"));
											pp.sendMessage(new TextComponent("§6§lIssuer: §r" + displayName));
											pp.sendMessage(new TextComponent("§6§lType: §7MUTE"));
											pp.sendMessage(new TextComponent("§6§lPunished player: §r" + odn));
											pp.sendMessage(new TextComponent("§6§lLength: §7" + length + core.getTimeUtils().timeUnit(lengthUnit).toString().toLowerCase() + "(s)"));
											pp.sendMessage(new TextComponent("§6§lReason: §7" + reason));
											pp.sendMessage(new TextComponent("§7------------------------------"));
										}
										if(pp.getUniqueId().toString().equals(offenderUUID)) {
											Title t = core.getProxy().createTitle();
											t.title(new TextComponent("§c§lMUTE")).subTitle(new TextComponent("§7" + reason)).fadeIn(20).stay(60).fadeOut(20);
											t.send(pp);
										}
									}
								}else{
									cs.sendMessage(new TextComponent(core.getPrefix() + "§aSuccessfully punished a player!"));
								}
							}else{
								cs.sendMessage(new TextComponent(core.getPrefix() + "§cAn error occured while parsing the expiration date!"));
							}
						}else{
							cs.sendMessage(new TextComponent(core.getPrefix() + "§cYou must specify a reason!"));
						}
					}else{
						cs.sendMessage(new TextComponent(core.getPrefix() + "§cYou must specify a valid length and time §cunit!"));
					}
				}else{
					cs.sendMessage(new TextComponent(core.getPrefix() + "§cLooks like this player has never played on §cour network."));
				}
				
			}else{
				cs.sendMessage(new TextComponent(core.getPrefix() + "§cSorry, but you are not allowed to do that."));
			}
		}
	}
	
	//Data used in punishing methods in order to display correct messages.
	public String getSenderData(CommandSender cs) {
		String senderData = "";
		if(cs instanceof ConsoleCommandSender) {
			senderData = "CONSOLE;null;§b§lCONSOLE";
		}else if(cs instanceof ProxiedPlayer) {
			ProxiedPlayer pp = (ProxiedPlayer) cs;
			senderData = pp.getUniqueId().toString() + ";" + pp.getServer().getInfo().getName() + ";" + pp.getDisplayName();
		}
		return senderData;
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
		cs.sendMessage(new TextComponent("§e§ §a/warn <player> <reason> §e - §7Warns a player."));
		cs.sendMessage(new TextComponent("§e§ §a/mute <player> <length> <unit> <reason> §e - §7Mutes a player."));
		cs.sendMessage(new TextComponent("§e§ §a/kick <player> <reason> §e - §7Adds player to the §7group."));
		cs.sendMessage(new TextComponent("§e§ §a/tb <player> <length> <unit> <reason>§e - §7Temporarily bans a player."));
		cs.sendMessage(new TextComponent("§e§ §a/ban <player> <reason> §e - §7Permanently bans a player."));
		cs.sendMessage(new TextComponent("§e§ §a/appeal <punishmentID> §e - §7Appeals a punishment."));
	}

}
