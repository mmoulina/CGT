package com.poplicus.crawler.codegen.utilities.config;

public class CodeGenConfigCache {
	
	private static CodeGenConfigCache configCache = null;
	
	private CodeGenDefConfig configuration = null;
	
	private CodeGenConfigCache() {
		
	}
	
	public static CodeGenConfigCache getInstance() {
		if(configCache == null) {
			configCache = new CodeGenConfigCache();
		}
		return configCache;
	}
	
	public void setConfigurations(CodeGenDefConfig configuration) {
		this.configuration = configuration;
	}
	
	public CodeGenDefConfig getConfigurations() {
		return configuration;
	}
	
}
