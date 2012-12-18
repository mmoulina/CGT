package com.poplicus.crawler.codegen.factory;

import java.util.HashMap;
import java.util.Map;

import com.poplicus.crawler.codegen.definitions.DatumDefinition;

public class DatumCache {
	
	private static DatumCache datumCache = null;
	
	private static Map<String, DatumDefinition> datumMap = new HashMap<String, DatumDefinition>();
	
	private DatumCache() {
		
	}
	
	public static DatumCache getInstance() {
		if(datumCache == null) {
			datumCache = new DatumCache();
		}
		return datumCache;
	}
	
	public void put(String key, DatumDefinition con) {
		datumMap.put(key, con);
	}
	
	public DatumDefinition get(String key) {
		return datumMap.get(key);
	}
	
	public void remove(String key) {
		datumMap.remove(key);
	}
	
}
