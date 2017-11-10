package net.aurios.auriosbungee;

import java.util.logging.Logger;

import net.aurios.auriosbungee.api.FileManager;
import net.aurios.auriosbungee.api.GroupsAPI;
import net.aurios.auriosbungee.api.MySQL;
import net.aurios.auriosbungee.api.PlayerDataAPI;
import net.aurios.auriosbungee.api.PunishmentAPI;
import net.aurios.auriosbungee.commands.Group;
import net.aurios.auriosbungee.commands.Warn;
import net.aurios.auriosbungee.listeners.BungeeListeners;
import net.md_5.bungee.api.plugin.Plugin;

public class AuriosBungee extends Plugin {
	
	private FileManager fileManager;
	private MySQL mysql;
	private GroupsAPI groupsAPI;
	private PlayerDataAPI pdAPI;
	private PunishmentAPI punishmentAPI;
	
	@Override
	public void onEnable() {
		fileManager = new FileManager(this);
		mysql = new MySQL(this);
		groupsAPI = new GroupsAPI(this);
		pdAPI = new PlayerDataAPI(this);
		punishmentAPI = new PunishmentAPI(this);
		if(!fileManager.getFile("plugins//AuriosBungee//", "mysql.yml").exists()) mysql.createMySQLFile("plugins//AuriosBungee//");
		mysql.connect();
		mysql.update("CREATE TABLE IF NOT EXISTS playerData(ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, Username VARCHAR(16), UUID VARCHAR(64), Groups VARCHAR(128), JoinDate DATETIME, LastSeen DATETIME, Joins INT, Balance INT, DTCKills INT, DTCDeaths INT, EmailAddress VARCHAR(30), PasswordHash VARCHAR(90), LastPunishment SMALLINT, Votes SMALLINT, VoteTokens SMALLINT);");
		mysql.update("CREATE TABLE IF NOT EXISTS groupdata(ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, Name VARCHAR(16), Permissions VARCHAR(6000), Prefix VARCHAR(48), ChatColor VARCHAR(16), Value INT(2));");
		mysql.update("CREATE TABLE IF NOT EXISTS punishmentdata(ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, Staff VARCHAR(64), Punished VARCHAR(64), PunishmentType VARCHAR(16), Reason VARCHAR(256), Date VARCHAR(20), ExpirationDate VARCHAR(20), Appealed TINYINT(1), AppealedBy VARCHAR(64));");
		registerCommandsAndListeners();
		groupsAPI.createNewGroup(getProxy().getConsole(), "Member", 1);
		groupsAPI.permissions = groupsAPI.initializeGroupPermissions();
	}
	
	public Logger getAuriosLogger() {
		return getLogger();
	}
	
	public FileManager getFileManager() {
		return fileManager;
	}
	
	public MySQL getMySQL() {
		return mysql;
	}
	
	public GroupsAPI getGroupsAPI() {
		return groupsAPI;
	}
	
	public PlayerDataAPI getPlayerDataAPI() {
		return pdAPI;
	}
	
	public PunishmentAPI getPunishmentAPI() {
		return punishmentAPI;
	}
	
	public String getPrefix() {
		return "§7[§6§lAURIOS§7] ";
	}
	
	private void registerCommandsAndListeners() {
		getProxy().getPluginManager().registerCommand(this, new Group(this));
		getProxy().getPluginManager().registerCommand(this, new Warn(this));
		
		getProxy().getPluginManager().registerListener(this, new BungeeListeners(this));
	}

}
