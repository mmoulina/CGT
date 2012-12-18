package com.poplicus.crawler.codegen.utilities.config;

import java.util.ArrayList;
import java.util.List;

public class TableDefConfig {
	
	private String name = null;
	
	private String displayName = null;
	
	private boolean usedInBidCrawler = false;
	
	private boolean usedInOrgCrawler = false;
	
	private boolean usedInPlanHolderCrawler = false;
	
	private boolean usedInBidResultsCrawler = false;
	
	private boolean displayInUI = true;
	
	private List<ColumnDefConfig> columnDefinitions = new ArrayList<ColumnDefConfig>();
	
	private List<ColumnDefConfig> defaultValueColumns = new ArrayList<ColumnDefConfig>();
	
	public void addDefaultValueColumn(ColumnDefConfig colDef) {
		this.defaultValueColumns.add(colDef);
	}
	
	public void setDefaultValueColumns(List<ColumnDefConfig> colDefs) {
		this.defaultValueColumns = colDefs;
	}
	
	public List<ColumnDefConfig> getDefaultValueColumns() {
		return this.defaultValueColumns;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isUsedInBidCrawler() {
		return usedInBidCrawler;
	}

	public void setUsedInBidCrawler(boolean usedInBidCrawler) {
		this.usedInBidCrawler = usedInBidCrawler;
	}

	public boolean isUsedInOrgCrawler() {
		return usedInOrgCrawler;
	}

	public void setUsedInOrgCrawler(boolean usedInOrgCrawler) {
		this.usedInOrgCrawler = usedInOrgCrawler;
	}

	public boolean isUsedInPlanHolderCrawler() {
		return usedInPlanHolderCrawler;
	}

	public void setUsedInPlanHolderCrawler(boolean usedInPlanHolderCrawler) {
		this.usedInPlanHolderCrawler = usedInPlanHolderCrawler;
	}

	public boolean isUsedInBidResultsCrawler() {
		return usedInBidResultsCrawler;
	}

	public void setUsedInBidResultsCrawler(boolean usedInBidResultsCrawler) {
		this.usedInBidResultsCrawler = usedInBidResultsCrawler;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addColumnDefinition(ColumnDefConfig colDef) {
		this.columnDefinitions.add(colDef);
	}
	
	public void setColumnDefinition(List<ColumnDefConfig> columnDefinitions) {
		this.columnDefinitions = columnDefinitions;
	}
	
	public List<ColumnDefConfig> getColumnDefinition() {
		return this.columnDefinitions;
	}
	
	public boolean isDisplayInUI() {
		return displayInUI;
	}

	public void setDisplayInUI(boolean displayInUI) {
		this.displayInUI = displayInUI;
	}

}
