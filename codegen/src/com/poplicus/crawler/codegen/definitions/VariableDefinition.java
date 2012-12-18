package com.poplicus.crawler.codegen.definitions;

public class VariableDefinition implements ExpressionElementsDef {
	
	private String name = null;
	
	private String type = null;
	
	private String value = null;
	
	public VariableDefinition() {
		
	}
	
	public VariableDefinition(String type, String name, String value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer("[Variable name : ");
		sbf.append(name).append(", type : ").append(type).append(", value : ").append(value).append("]");
		return sbf.toString();
	}
	
}
