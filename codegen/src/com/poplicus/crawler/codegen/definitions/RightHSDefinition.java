package com.poplicus.crawler.codegen.definitions;

public class RightHSDefinition {

	private String type = null;
	
	private ActionDefinition actionDef = null;
	
	private FunctionDefinition functionDef = null;
	
	private ExpressionDefinition expressionDef = null;
	
	public ExpressionDefinition getExpressionDef() {
		return expressionDef;
	}

	public void setExpressionDef(ExpressionDefinition expressionDef) {
		this.expressionDef = expressionDef;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ActionDefinition getActionDef() {
		return actionDef;
	}

	public void setActionDef(ActionDefinition actionDef) {
		this.actionDef = actionDef;
	}

	public FunctionDefinition getFunctionDef() {
		return functionDef;
	}

	public void setFunctionDef(FunctionDefinition functionDef) {
		this.functionDef = functionDef;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer("[RHS Def Type : ");
		sbf.append(type).append(", ");
		if(expressionDef != null) {
			sbf.append(expressionDef.toString());
		}
		
		return sbf.toString();
	}

}
