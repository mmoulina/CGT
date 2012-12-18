package com.poplicus.crawler.codegen.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.poplicus.crawler.codegen.definitions.ExpressionDefinition;

public class ExpressionCache {

	private static ExpressionCache expressionCache = null;
	
	private static Map<String, ExpressionDefinition> expressionMap = new HashMap<String, ExpressionDefinition>();
	
	private ExpressionCache() {
		
	}
	
	public static ExpressionCache getInstance() {
		if(expressionCache == null) {
			expressionCache = new ExpressionCache();
		}
		return expressionCache;
	}
	
	public void put(String key, ExpressionDefinition con) {
		expressionMap.put(key, con);
	}
	
	public ExpressionDefinition get(String key) {
		return expressionMap.get(key);
	}
	
	public void remove(String key) {
		expressionMap.remove(key);
	}

	public List<String> getExpressionNames() {
		List<String> conNames = new ArrayList<String>();
		Iterator<String> keys = expressionMap.keySet().iterator();
		while(keys.hasNext()) {
			conNames.add(keys.next());
		}
		Collections.sort(conNames, new ExpressionNameComparator());
		return conNames;
	}
	
	class ExpressionNameComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			int one = new Integer(o1.substring(o1.lastIndexOf(":") + 1)).intValue();
			int two = new Integer(o2.substring(o2.lastIndexOf(":") + 1)).intValue();
			return one < two ? -1 : (one == two ? 0 : 1);
			
		}
		
	}

}
