package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.List;

public class DataGroup {
	
	private String startTag = null;
	
	private String endTag = null;
	
	private List<DataSet> dataSets = new ArrayList<DataSet>();
	
	public DataGroup() {
		
	}

	public DataGroup(String startTag, String endTag) {
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

	public List<DataSet> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<DataSet> dataSets) {
		this.dataSets = dataSets;
	}
	
	public void addDataSet(DataSet dataSet) {
		this.dataSets.add(dataSet);
	}
	
}
