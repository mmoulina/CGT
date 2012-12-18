package com.poplicus.crawler.codegen.definitions;

public class ConditionNameGenerator {
	
	private static ConditionNameGenerator conNameGen = null;

	private static StringBuffer prefixString = new StringBuffer("pop:crwl:cons:");
	
	private static int counter = 0;
	
	private ConditionNameGenerator() {
		
	}
	
	public static ConditionNameGenerator getInstance() {
		if(conNameGen == null) {
			conNameGen = new ConditionNameGenerator();
		}
		return conNameGen;
	}
	
	public String getUniqueName() {
		StringBuffer uniqueName = new StringBuffer(prefixString);
		uniqueName.append((new Integer(++ counter)).toString());
		return uniqueName.toString();
	}
	
}
 