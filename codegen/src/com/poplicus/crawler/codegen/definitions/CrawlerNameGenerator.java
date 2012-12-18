package com.poplicus.crawler.codegen.definitions;

public class CrawlerNameGenerator {
	
	private static CrawlerNameGenerator crawlerNameGen = null;

	private static StringBuffer prefixString = new StringBuffer("pop:crwl:name:");
	
	private static int crawlerNameCounter = 0;
	
	private CrawlerNameGenerator() {
		
	}
	
	public static CrawlerNameGenerator getInstance() {
		if(crawlerNameGen == null) {
			crawlerNameGen = new CrawlerNameGenerator();
		}
		return crawlerNameGen;
	}
	
	public String getUniqueName() {
		StringBuffer uniqueName = new StringBuffer(prefixString);
		uniqueName.append((new Integer(++ crawlerNameCounter)).toString());
		return uniqueName.toString();
	}
	
}
