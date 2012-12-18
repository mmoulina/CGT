package com.poplicus.crawler.codegen.definitions;

public class OperatorDefinition implements ExpressionElementsDef {

	private String operator = null;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer("Operator Definition : ");
		sbf.append(operator);
		return sbf.toString();
	}
	
}
