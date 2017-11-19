package net.aurios.auriosbungee.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.aurios.auriosbungee.AuriosBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

public class GroupsAPI {
	
	public HashMap<String, List<String>> permissions = new HashMap<String, List<String>>(); //Group name, list of permissions
	
	AuriosBungee core;
	public GroupsAPI(AuriosBungee core) {
		this.core = core;
	}
	
	public HashMap<String, List<String>> initializeGroupPermissions() {
		HashMap<String, List<String>> ip = new HashMap<String, List<String>>(); //Initialized permissions -> ip
		ResultSet rs = core.getMySQL().getResult("SELECT * FROM groupdata");
		try {
			while(rs.next()) {
				ip.put(rs.getString("Name"), new ArrayList<String>());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(String s : ip.keySet()) {
			List<String> gp = ip.get(s);//gp -> Group permissions
			String perms = (String) core.getMySQL().get("Permissions", "groupdata", "Name", s);
			if(perms != null && !perms.isEmpty()) {
				if(perms.contains(";")) {
					String[] sperms = perms.split(";"); //sperms -> split permissions
					for(int i = 0; i < sperms.length; i++) {
						gp.add(sperms[i]);
					}
				}else{
					gp.add(perms);
				}
			}
			ip.put(s, gp);
		}
		return ip;
	}
	
	public boolean groupExists(String group) {
		String gn = (String)core.getMySQL().get("Name", "groupdata", "Name", group);
		return gn != null && !gn.isEmpty();
	}
	
	private String getRawPlayerGroups(String uuid) {
		return (String) core.getMySQL().get("Groups", "playerData", "UUID", uuid);
	}
	
	public List<String> getPlayerGroups(String uuid) {
		List<String> groups = new ArrayList<String>();
		String pg = getRawPlayerGroups(uuid);
		if(pg != null && !pg.isEmpty()) {
			if(pg.contains(";")) {
				String[] g = getRawPlayerGroups(uuid).split(";");
				for(int i = 0; i < g.length; i++) {
					groups.add(g[i]);
				}
			}else{
				groups.add(pg);
			}
		}
		return groups;
	}
	
	public boolean groupContainsPermission(String group, String permission) {
		if(groupExists(group)) {
			group = group.substring(0, 1).toUpperCase() + group.substring(1).toLowerCase();
			List<String> prs = permissions.get(group);
			return prs.contains(permission);
		}
		return false;
	}
	
	public boolean hasPermission(CommandSender cs, String permission) {
		if(cs instanceof ConsoleCommandSender) return true;
		else if(cs instanceof ProxiedPlayer) {
			int x = 0;
			ProxiedPlayer pp = (ProxiedPlayer) cs;
			String uuid = pp.getUniqueId().toString();
			List<String> groups = getPlayerGroups(uuid);
			for(String g : groups) {
				if(groupContainsPermission(g, permission)) x+=1;
			}
			return x>0;
		}
		return false;
	}
	
	public void createNewGroup(CommandSender cs, String groupName, int value) {
		if(!groupExists(groupName)) {
			groupName = groupName.substring(0, 1).toUpperCase() + groupName.substring(1).toLowerCase();
			try{
				core.getMySQL().update("INSERT INTO groupdata(ID, Name, Permissions, Prefix, ChatColor, Value) VALUES ('0', '" + groupName + "', '', '', '', '"+value+"');");
				permissions.put(groupName, new ArrayList<String>());
				cs.sendMessage(new TextComponent(core.getPrefix() + "ßaYou've just created a new group."));
			}catch(Exception ex) {
				cs.sendMessage(new TextComponent(core.getPrefix() + "ßcSomething went wrong while creating a new group."));
			}
		}else{
			cs.sendMessage(new TextComponent(core.getPrefix() + "ßcThis group already exists."));
		}
	}
	
	public void removeGroup(CommandSender cs, String groupName) {
		if(groupExists(groupName)) {
			groupName = groupName.substring(0, 1).toUpperCase() + groupName.substring(1).toLowerCase();
			List<String> pig = getPlayersInGroup(groupName);//Players in group
			for(String uuid : pig) {
				removePlayerFromGroup(null, groupName, uuid);
			}
			permissions.remove(groupName, permissions.get(groupName));
			core.getMySQL().update("DELETE FROM groupdata WHERE Name='" + groupName + "';");
			cs.sendMessage(new TextComponent(core.getPrefix() + "ßaGroup successfully removed."));
		}else{
			cs.sendMessage(new TextComponent(core.getPrefix() + "ßcGroup does not exist."));
		}
	}
	
	public void addPlayerToGroup(CommandSender cs, String group, String playerUUID) {
		if(groupExists(group)) {
			group = group.substring(0, 1).toUpperCase() + group.substring(1).toLowerCase();
			if(!getPlayerGroups(playerUUID).contains(group)) {
				try{
					String pg = getRawPlayerGroups(playerUUID);
					pg += group + ";";
					core.getMySQL().set("playerdata", "Groups", pg, "UUID", playerUUID);
					ProxyServer.getInstance().getPlayer(UUID.fromString(playerUUID)).sendMessage(new TextComponent(core.getPrefix() + "ßaYou were added to the group ß7" + group + "ßa. ßcßlPlease rejoin now."));
				}catch(Exception ex) {
					cs.sendMessage(new TextComponent(core.getPrefix() + "ßcSomething went wrong while adding player to the group."));
					ex.printStackTrace();
				}
			}else{
				cs.sendMessage(new TextComponent(core.getPrefix() + "ßcUser already belongs to that group."));
			}
		}else{
			cs.sendMessage(new TextComponent(core.getPrefix() + "ßcGroup does not exist."));
		}
	}
	
	public void removePlayerFromGroup(CommandSender cs, String group, String playerUUID) {
		if(groupExists(group)) {
			group = group.substring(0, 1).toUpperCase() + group.substring(1).toLowerCase();
			if(getRawPlayerGroups(playerUUID).contains(group)) {
				String pg = getRawPlayerGroups(playerUUID).replace(group + ";", "");
				core.getMySQL().set("playerdata", "Groups", pg, "UUID", playerUUID);
				ProxyServer.getInstance().getPlayer(UUID.fromString(playerUUID)).sendMessage(new TextComponent(core.getPrefix() + "ßaYou were removed from the group ß7" + group + "ßa. ßcßlPlease rejoin now."));
			}else{
				if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcUser does not belong to that group yet."));
			}
		}else{
			if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcGroup does not exist."));
		}
	}
	
	public void addPermissionToTheGroup(CommandSender cs, String group, String permission) {
		if(groupExists(group)) {
			group = group.substring(0, 1).toUpperCase() + group.substring(1).toLowerCase();
			if(!groupContainsPermission(group, permission)) {
				try{
					List<String> p = permissions.get(group);
					List<String> np = new ArrayList<String>();
					p.forEach(perm -> np.add(perm));
					np.add(permission);
					permissions.replace(group, p, np);
					core.getMySQL().set("groupdata", "Permissions", toRawString(np), "Name", group);
					if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßaPermission added!"));
				}catch(Exception ex) {
					if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcSomething went wrong while adding permission to a group."));
					ex.printStackTrace();
				}
			}else{
				if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcGroup already contains that permission."));
			}
		}else{
			if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcGroup does not exist."));
		}
	}
	
	public void removePermissionFromTheGroup(CommandSender cs, String group, String permission) {
		if(groupExists(group)) {
			group = group.substring(0, 1).toUpperCase() + group.substring(1).toLowerCase();
			if(groupContainsPermission(group, permission)) {
				try{
					List<String> p = permissions.get(group);
					List<String> np = new ArrayList<String>();
					p.forEach(perm -> np.add(perm));
					np.remove(permission);
					permissions.replace(group, p, np);
					core.getMySQL().set("groupdata", "Permissions", toRawString(np), "Name", group);
					if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßaPermission removed!"));
				}catch(Exception ex) {
					if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcSomething went wrong while removing permission from a group."));
					ex.printStackTrace();
				}
			}else{
				if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcGroup does not contain that permission yet."));
			}
		}else{
			if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcGroup does not exist."));
		}
	}
	
	public String toRawString(List<String> list) {
		String s = "";
		for(int i = 0; i < list.size(); i++) {
			s += list.get(i) + ";";
		}
		return s;
	}
	
	public String getPrefix(String group) {
		return (String) core.getMySQL().get("Prefix", "groupdata", "Name", group);
	}
	
	public void setPrefix(CommandSender cs, String group, String newPrefix) {
		if(groupExists(group)) {
			try{
				core.getMySQL().set("groupdata", "Prefix", newPrefix, "Name", group);
				if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßaPrefix successfully updated!"));
			}catch(Exception ex) {
				if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßaSomething went wrong while setting a new prefix."));
				ex.printStackTrace();
			}
		}else{
			if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcGroup does not exist."));
		}
	}
	
	public String getChatColor(String group) {
		return (String) core.getMySQL().get("ChatColor", "groupdata", "Name", group);
	}
	
	public void setChatColor(CommandSender cs, String group, String newChatColor) {
		if(groupExists(group)) {
			try{
				core.getMySQL().set("groupdata", "ChatColor", newChatColor, "Name", group);
				if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßaChatColor successfully updated!"));
			}catch(Exception ex) {
				if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßaSomething went wrong while setting a new chat color."));
				ex.printStackTrace();
			}
		}else{
			if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcGroup does not exist."));
		}
	}
	
	public void setValue(CommandSender cs, String group, int value) {
		if(groupExists(group)) {
			try{
				core.getMySQL().set("groupdata", "Value", value, "Name", group);
				if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßaValue successfully updated!"));
			}catch(Exception ex) {
				if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßaSomething went wrong while setting a new value."));
				ex.printStackTrace();
			}
		}else{
			if(cs != null) cs.sendMessage(new TextComponent(core.getPrefix() + "ßcGroup does not exist."));
		}
	}
	
	public String getTheMostValuablePlayerGroup(List<String> groups) {
		String toReturn = "";
		int value = 0;
		for(String g : groups) {
			if(groupExists(g)) {
				int gv = (int)core.getMySQL().get("Value", "groupdata", "Name", g); //Group value
				if(gv > value) {
					toReturn = g;
					value = gv;
				}
			}
		}
		return toReturn;
	}
	
	public void sendHelp(CommandSender cs) {
		cs.sendMessage(new TextComponent("ß7ßm-------------------=ßbßm[ßr ß6ßlGROUPS ßbßm]ß7ßm=-------------------"));
		cs.sendMessage(new TextComponent("ßeóè ßa/group create <name> <value> ße - ß7Creates a new group with ß7a given value."));
		cs.sendMessage(new TextComponent("ßeóè ßa/group remove <name> ße - ß7Completely removes the existing ß7group."));
		cs.sendMessage(new TextComponent("ßeóè ßa/group addplayer <name> <player> ße - ß7Adds player to the ß7group."));
		cs.sendMessage(new TextComponent("ßeóè ßa/group removeplayer <name> <player> ße - ß7Removes player ß7from the group."));
		cs.sendMessage(new TextComponent("ßeóè ßa/group addperm <name> <permission> ße - ß7Adds permission to ß7the group."));
		cs.sendMessage(new TextComponent("ßeóè ßa/group remperm <name> <permission> ße - ß7Removes permission ß7from the group."));
		cs.sendMessage(new TextComponent("ßeóè ßa/group setprefix <name> <prefix> ße - ß7Sets group's prefix."));
		cs.sendMessage(new TextComponent("ßeóè ßa/group setchatcolor <name> <color> ße - ß7Sets group's chat color."));
		cs.sendMessage(new TextComponent("ßeóè ßa/group setvalue <name> <value> ße - ß7Sets group's value."));
	}
	
	public List<String> getPlayersInGroup(String group) {
		List<String> players = new ArrayList<String>();
		ResultSet rs = core.getMySQL().getResult("SELECT * FROM playerdata");
		try{
			while(rs.next()) {
				String playerUUID = rs.getString("UUID");
				String groups = rs.getString("Groups");
				if(groups.contains(group + ";")) players.add(playerUUID);
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return players;
	}

}
