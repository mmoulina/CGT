package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.List;

public class DataGroupDefinition {

	private String startTag = null;
	
	private String endTag = null;
	
	private List<DataSetDefinition> dataSets = new ArrayList<DataSetDefinition>();
	
	public DataGroupDefinition() {
		
	}

	public DataGroupDefinition(String startTag, String endTag) {
		this.startTag = startTag;
		this.endTag = endTag;
	}
	
	public String getStartTag() {
		return startTag;
	}

	public void setStartTag(String startTag) {
		this.startTag = startTag;
	}

	public String getEndTag() {
		return endTag;
	}

	public void setEndTag(String endTag) {
		this.endTag = endTag;
	}

	public List<DataSetDefinition> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<DataSetDefinition> dataSets) {
		this.dataSets = dataSets;
	}
	
	public void addDataSet(DataSetDefinition dataSet) {
		this.dataSets.add(dataSet);
	}

}
