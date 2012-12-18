package com.poplicus.crawler.codegen.definitions;

public class DatumNameGenerator {

	private static DatumNameGenerator datumNameGen = null;

	private static StringBuffer prefixString = new StringBuffer("pop:crwl:datum:name:");
	
	private static int datumNameCounter = 0;
	
	private DatumNameGenerator() {
		
	}
	
	public static DatumNameGenerator getInstance() {
		if(datumNameGen == null) {
			datumNameGen = new DatumNameGenerator();
		}
		return datumNameGen;
	}
	
	public String getUniqueName() {
		StringBuffer uniqueName = new StringBuffer(prefixString);
		uniqueName.append((new Integer(++ datumNameCounter)).toString());
		return uniqueName.toString();
	}

}
