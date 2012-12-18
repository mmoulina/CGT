package com.poplicus.crawler.codegen.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.poplicus.crawler.codegen.Condition;
import com.poplicus.crawler.codegen.definitions.ConditionDefinition;

public class ConditionCache {
	
	private static ConditionCache conditionCache = null;
	
	private static Map<String, Condition> conditionMap = new HashMap<String, Condition>();
	
	private ConditionCache() {
		
	}
	
	public static ConditionCache getInstance() {
		if(conditionCache == null) {
			conditionCache = new ConditionCache();
		}
		return conditionCache;
	}
	
	public void put(String key, Condition con) {
		conditionMap.put(key, con);
	}
	
	public Condition get(String key) {
		return conditionMap.get(key);
	}
	
	public void remove(String key) {
		conditionMap.remove(key);
	}
	
	public ConditionDefinition getConditionDefinition(String key) {
		Condition condition = conditionMap.get(key);
		
		ConditionDefinition condDef = new ConditionDefinition(false);
		if(condition.getLeftSideValue() instanceof Condition) {
			condDef.setLshOperand(((Condition) condition.getLeftSideValue()).toStringBuffer().toString());
		} else {
			condDef.setLshOperand((String) condition.getLeftSideValue());
		}
		if(condition.getRightSideValue() instanceof Condition) {
			condDef.setRhsOperand(((Condition) condition.getRightSideValue()).toStringBuffer().toString());
		} else {
			condDef.setRhsOperand((String) condition.getRightSideValue());
		}

		condDef.setOperator(condition.getOperator().toString());
		condDef.setName(condition.getName());
		condDef.setSuperCondition(condition.isSuperCondition());
		
		return condDef;
	}
	
	public List<String> getConditionNames() {
		List<String> conNames = new ArrayList<String>();
		Iterator<String> keys = conditionMap.keySet().iterator();
		while(keys.hasNext()) {
			conNames.add(keys.next());
		}
		Collections.sort(conNames, new ConditionNameComparator());
		return conNames;
	}
	
	class ConditionNameComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			int one = new Integer(o1.substring(o1.lastIndexOf(":") + 1)).intValue();
			int two = new Integer(o2.substring(o2.lastIndexOf(":") + 1)).intValue();
			return one < two ? -1 : (one == two ? 0 : 1);
			
		}
		
	}
	
}
