package com.poplicus.crawler.codegen;

import java.util.regex.Pattern;

public class Indentation {
	
	public static int INDENTATION_LEVEL_0 = 0;
	
	public static int INDENTATION_LEVEL_1 = 1;
	
	public static int INDENTATION_LEVEL_2 = 2;
	
	public static int INDENTATION_LEVEL_3 = 3;
	
	public static int INDENTATION_LEVEL_4 = 4;
	
	public static int INDENTATION_LEVEL_5 = 5;
	
	public static int INDENTATION_LEVEL_6 = 6;
	
	public static int INDENTATION_LEVEL_7 = 7;
	
	public static int INDENTATION_LEVEL_8 = 8;

	public static int INDENTATION_LEVEL_9 = 9;
	
	public static int INDENTATION_LEVEL_10 = 10;
	
	private int level = 0;
	
	public Indentation(int level) {
		this.level = level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}
	
	public StringBuffer getTabs() {
		StringBuffer tabs = new StringBuffer();
		int counter = 0;
		while(counter != this.level) {
			tabs.append(Constants.TAB);
			counter ++;
		}
		return tabs;
	}
	
	public StringBuffer getAdditionalTabs(int customLevel) {
		StringBuffer tabs = new StringBuffer();
		int counter = 0;
		int tempLevel = this.level + customLevel;
		while(counter != tempLevel) {
			tabs.append(Constants.TAB);
			counter ++;
		}
		return tabs;
	}
	
	public StringBuffer getTabsForNextLevel() {
		StringBuffer tabs = new StringBuffer();
		int counter = 0;
		int tempLevel = this.level + 1;
		while(counter != tempLevel) {
			tabs.append(Constants.TAB);
			counter ++;
		}
		return tabs;
	}
	
	public StringBuffer getTabsForNextLevel(StringBuffer currentTabs) {
		StringBuffer tabs = new StringBuffer();
		int counter = 0;
		int tempLevel = getNumberOfTabs(currentTabs) + 1;
		while(counter != tempLevel) {
			tabs.append(Constants.TAB);
			counter ++;
		}
		return tabs;
	}
	
	public int getNumberOfTabs(StringBuffer currentTabs) {
		Pattern pattern = Pattern.compile("[$]");
		String tempTabs = currentTabs.toString();
		tempTabs = tempTabs.replace("\t", "$,");
		tempTabs = tempTabs.substring(0, tempTabs.lastIndexOf(","));
		String[] arrTabs = pattern.split(tempTabs);
		return arrTabs.length;
	}

}
