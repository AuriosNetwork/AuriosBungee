package net.aurios.auriosbungee.api;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.aurios.auriosbungee.AuriosBungee;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MySQL {

    private AuriosBungee core;
    private static String HOST = "";
    private static String DATABASE ="";
    private static String USER = "";
    private static String PASSWORD = "";
    private static int PORT = 3306;

    private static java.sql.Connection con;
    private boolean isConnected;
    
    public MySQL(AuriosBungee core) {
        this.core = core;
    }

    public void createMySQLFile(String path) {
    	core.getFileManager().createFile(path, "mysql.yml");
        File f = core.getFileManager().getFile(path, "mysql.yml");
        Configuration cfg = core.getFileManager().getConfiguration(YamlConfiguration.class, f);
        
        cfg.set("username", "root");
        cfg.set("password", "");
        cfg.set("database", "aurios_network");
        cfg.set("host", "localhost");
        cfg.set("port", 3306);
        try{
        	ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, f);
        	core.getAuriosLogger().info("Saved 'mysql.yml'.");
        }catch(IOException ex) {
        	core.getAuriosLogger().severe("Could not save file 'mysql.yml'.");
        }
    }

    public void connect() {
    	File mysqlFile = core.getFileManager().getFile("plugins//AuriosBungee//", "mysql.yml");
    	Configuration cfg = core.getFileManager().getConfiguration(YamlConfiguration.class, mysqlFile);
        HOST = cfg.getString("host");
        DATABASE = cfg.getString("database");
        USER = cfg.getString("username");
        PASSWORD = cfg.getString("password");
        PORT = cfg.getInt("port");
        if(!isConnected) {
        	try{
        		con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?autoReconnect=true", USER, PASSWORD);
        		core.getAuriosLogger().info("§aSuccessfully connected to MySQL database!");
        	}catch(SQLException ex) {
        		core.getAuriosLogger().severe("Could not connect to MySQL database, please check your MySQL settings!");
        	}
        }
    }

    public void close() {
    	if(isConnected()) {
    		try{
    			con.close();
    			con = null;
    			core.getAuriosLogger().info("§aSuccessfully closed MySQL connection!");
    		}catch(SQLException ex) {
    			ex.printStackTrace();
    		}
    	}
    }
    
    public boolean isConnected() {
    	return con != null;
    }

    public void update(String qry) {
    	if(isConnected()) {
    		try{
    			con.createStatement().executeUpdate(qry);
    		}catch(SQLException ex) {
    			ex.printStackTrace();
    		}
    	}
    }

    public ResultSet getResult(String qry) {
    	if(isConnected()){
    		try{
    			return con.createStatement().executeQuery(qry);
    		}catch(SQLException ex) {
    			ex.printStackTrace();
    		}
    	}
    	return null;
    }
    
    public int countRows(String tableName) throws SQLException {
    	Statement stmt = null;
    	ResultSet rs = null;
    	int rowCount = -1;
    	try{
    		stmt = con.createStatement();
    		rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
    		rs.next();
    		rowCount = rs.getInt(1);
    	}finally{
    		rs.close();
    		stmt.close();
    	}
    	return rowCount;
    }
    
	public Object get(String whatAreWeLookingFor, String fromTable, String where, String whereValue) {
		Object obj = null;
		try{
			ResultSet rs = getResult("SELECT " + whatAreWeLookingFor + " FROM " + fromTable + " WHERE " + where + "='" + whereValue + "';");
			if(rs.next()) obj = rs.getObject(whatAreWeLookingFor);
			return obj;
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return obj;
	}
	
	public void set(String tableName, String whatAreWeSetting, Object newValue, String where, String whereValue) {
		update("UPDATE " + tableName + " SET " + whatAreWeSetting + "='" + newValue + "' WHERE " + where + "='" + whereValue + "';");
	}

}
