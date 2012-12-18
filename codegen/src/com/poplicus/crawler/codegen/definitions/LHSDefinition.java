package com.poplicus.crawler.codegen.definitions;

public class LHSDefinition {

	private String type = null;
	
	private VariableDefinition varDefinition = null;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setVariableDefinition(VariableDefinition varDef) {
		this.varDefinition = varDef;
	}
	
	public VariableDefinition getVariableDefinition() {
		return this.varDefinition;
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer("LHS Def Type : ");
		sbf.append(type).append(", ").append(varDefinition).append("]");
		return sbf.toString();
	}
	
}
