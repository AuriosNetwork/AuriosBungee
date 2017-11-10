package net.aurios.auriosbungee.api;

import java.io.File;
import java.io.IOException;

import net.aurios.auriosbungee.AuriosBungee;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;

public class FileManager {
	
	AuriosBungee core;
	public FileManager(AuriosBungee core) {
		this.core = core;
	}
	
	public File getFile(String path, String fileName) {
		return new File(path, fileName);
	}
	
	public void createFile(String path, String fileName) {
		File op = new File(path); //op -> our path
		if(!op.isDirectory()) op.mkdirs();
		File of = new File(op, fileName);
		if(!of.exists())
			try {
				of.createNewFile();
			} catch (IOException e) {
				core.getAuriosLogger().severe("Could not create a new file named '" + fileName + "'.");
				e.printStackTrace();
			}
	}
	
	public Configuration getConfiguration(Class<? extends ConfigurationProvider> provider, File file) {
		Configuration c = null;
		try {
			c = ConfigurationProvider.getProvider(provider).load(file);
		} catch (IOException e) {
			core.getAuriosLogger().severe("Could not get the configuration for file " + file.getName() + ".");
			e.printStackTrace();
		}
		return c;
	}

}
