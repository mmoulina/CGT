package com.poplicus.crawler.codegen.utilities.config;

public class ColumnDefConfig {
	
	private String name = null;
	
	private String dataType = null;
	
	private String variableName = null;
	
	private String defaultValue = null;
	
	private boolean passByValue = false;

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isPassByValue() {
		return passByValue;
	}

	public void setPassByValue(boolean passByValue) {
		this.passByValue = passByValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
}
