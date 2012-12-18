package com.poplicus.crawler.codegen.utilities.config;

import java.util.ArrayList;
import java.util.List;

public class ElementDefConfig {
	
	private String tagType = null;
	
	private List<String> supportedFields = new ArrayList<String>();

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
	
	public void addSupportedField(String supportedField) {
		this.supportedFields.add(supportedField);
	}
	
	public void setSupportedFields(List<String> supportedFields) {
		this.supportedFields = supportedFields;
	}
	
	public List<String> getSupportedFields() {
		return this.supportedFields;
	}
	
}
