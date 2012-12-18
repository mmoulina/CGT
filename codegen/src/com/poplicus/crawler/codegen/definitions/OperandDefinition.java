package com.poplicus.crawler.codegen.definitions;

public class OperandDefinition implements ExpressionElementsDef {
	
	private String operandName = null;

	private FunctionDefinition functionDef = null;
	
	private ActionDefinition actionDef = null;
	
	private VariableDefinition variableDef = null;
	
	private StringDefinition stringDef = null;
	
	private ExpressionDefinition expressionDef = null;

	public VariableDefinition getVariableDef() {
		return variableDef;
	}

	public void setVariableDef(VariableDefinition variableDef) {
		this.variableDef = variableDef;
	}

	public StringDefinition getStringDef() {
		return stringDef;
	}

	public void setStringDef(StringDefinition stringDef) {
		this.stringDef = stringDef;
	}

	public FunctionDefinition getFunctionDef() {
		return functionDef;
	}

	public void setFunctionDef(FunctionDefinition functionDef) {
		this.functionDef = functionDef;
	}

	public ActionDefinition getActionDef() {
		return actionDef;
	}

	public void setActionDef(ActionDefinition actionDef) {
		this.actionDef = actionDef;
	}

	public String getOperandName() {
		return operandName;
	}

	public void setOperandName(String operandName) {
		this.operandName = operandName;
	}
	
	public ExpressionDefinition getExpressionDef() {
		return expressionDef;
	}

	public void setExpressionDef(ExpressionDefinition expressionDef) {
		this.expressionDef = expressionDef;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer("Operand Definition : ");
		if(operandName != null) {
			sbf.append("Operand Name : ").append(operandName);
		}
		if(functionDef != null) {
			sbf.append(functionDef.toString());
		}
		if(actionDef != null) {
			sbf.append(actionDef.toString());
		}
		if(operandName == null && functionDef == null && actionDef == null) {
			sbf.append("null");
		}
		return sbf.toString();
	}

}
