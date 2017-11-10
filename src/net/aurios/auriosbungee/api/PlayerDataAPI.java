package net.aurios.auriosbungee.api;

import net.aurios.auriosbungee.AuriosBungee;
import net.aurios.auriosbungee.api.Enums.MathOperations;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerDataAPI {
	
	AuriosBungee core;
	public PlayerDataAPI(AuriosBungee core) {
		this.core = core;
	}
	
	public boolean playerExists(String uuid) {
		return core.getMySQL().get("UUID", "playerdata", "UUID", uuid) != null;
	}
	
	public void registerPlayer(ProxiedPlayer pp) {
		if(!playerExists(pp.getUniqueId().toString())) {
			core.getMySQL().update("INSERT INTO playerdata(ID, Username, UUID, Groups, JoinDate, LastSeen, Joins, Balance, DTCKills, DTCDeaths, EmailAddress, PasswordHash, LastPunishment, Votes, VoteTokens) VALUES"
					+ "('0', '" + pp.getName() + "', '" + pp.getUniqueId().toString() + "', 'Member;', NOW(), NULL, '1', '0', '0', '0', NULL, NULL, '0', '0', '0');");
		}
	}
	
	public int currentBalance(String uuid) {
		if(playerExists(uuid)) {
			return (int) core.getMySQL().get("Balance", "playerdata", "UUID", uuid);
		}
		return 0;
	}
	
	public void setBalance(String uuid, MathOperations mathOperation, int amount) {
		if(playerExists(uuid)) {
			if(mathOperation != null) {
				int newBalance = currentBalance(uuid);
				if(mathOperation == MathOperations.ADDITION) {
					newBalance += amount;
				}else if(mathOperation == MathOperations.SUBTRACTION) {
					newBalance -= amount;
				}
				if(newBalance >= 0) {
					core.getMySQL().set("playerdata", "Balance", newBalance, "UUID", uuid);
				}
			}
		}
	}

}
