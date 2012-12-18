package com.poplicus.crawler.codegen.definitions;

public class ExpressionNameGenerator {

	private static ExpressionNameGenerator expressionNameGen = null;

	private static StringBuffer prefixString = new StringBuffer("pop:crwl:exp:name:");
	
	private static int expressionNameCounter = 0;
	
	private ExpressionNameGenerator() {
		
	}
	
	public static ExpressionNameGenerator getInstance() {
		if(expressionNameGen == null) {
			expressionNameGen = new ExpressionNameGenerator();
		}
		return expressionNameGen;
	}
	
	public String getUniqueName() {
		StringBuffer uniqueName = new StringBuffer(prefixString);
		uniqueName.append((new Integer(++ expressionNameCounter)).toString());
		return uniqueName.toString();
	}

}
