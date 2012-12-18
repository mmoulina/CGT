package com.poplicus.crawler.codegen.definitions;

public class WebPageNameGenerator {

	private static WebPageNameGenerator webPageNameGen = null;

	private static StringBuffer prefixString = new StringBuffer("pop:crwl:webpage:name:");
	
	private static int webPageNameCounter = 0;
	
	private WebPageNameGenerator() {
		
	}
	
	public static WebPageNameGenerator getInstance() {
		if(webPageNameGen == null) {
			webPageNameGen = new WebPageNameGenerator();
		}
		return webPageNameGen;
	}
	
	public String getUniqueName() {
		StringBuffer uniqueName = new StringBuffer(prefixString);
		uniqueName.append((new Integer(++ webPageNameCounter)).toString());
		return uniqueName.toString();
	}

}
