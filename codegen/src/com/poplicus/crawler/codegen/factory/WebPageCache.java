package com.poplicus.crawler.codegen.factory;

import java.util.HashMap;
import java.util.Map;

import com.poplicus.crawler.codegen.definitions.WebPageDefinition;

public class WebPageCache {
	
	private static WebPageCache webPageCache = null;
	
	private static Map<String, WebPageDefinition> webPageMap = new HashMap<String, WebPageDefinition>();
	
	private WebPageCache() {
		
	}
	
	public static WebPageCache getInstance() {
		if(webPageCache == null) {
			webPageCache = new WebPageCache();
		}
		return webPageCache;
	}
	
	public void put(String key, WebPageDefinition con) {
		webPageMap.put(key, con);
	}
	
	public WebPageDefinition get(String key) {
		return webPageMap.get(key);
	}
	
	public void remove(String key) {
		webPageMap.remove(key);
	}
	
}
