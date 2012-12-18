package com.poplicus.crawler.codegen.factory;

import java.util.HashMap;
import java.util.Map;

import com.poplicus.crawler.codegen.definitions.CrawlerDefinition;

public class CrawlerCache {
	
	private static CrawlerCache crawlerCache = null;
	
	private static Map<String, CrawlerDefinition> crawlerMap = new HashMap<String, CrawlerDefinition>();
	
	private CrawlerCache() {
		
	}
	
	public static CrawlerCache getInstance() {
		if(crawlerCache == null) {
			crawlerCache = new CrawlerCache();
		}
		return crawlerCache;
	}
	
	public void put(String key, CrawlerDefinition con) {
		crawlerMap.put(key, con);
	}
	
	public CrawlerDefinition get(String key) {
		return crawlerMap.get(key);
	}
	
	public void remove(String key) {
		crawlerMap.remove(key);
	}
	
}
