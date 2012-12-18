package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.List;

public class ExpressionDefinition {
	
	private String name = null;

	private List<ExpressionElementsDef> expressionElements = new ArrayList<ExpressionElementsDef>();
	
	public ExpressionDefinition() {
		this.name = ExpressionNameGenerator.getInstance().getUniqueName();
	}
	
	public ExpressionDefinition(boolean generateUniqueName) {
		if(generateUniqueName) {
			this.name = ExpressionNameGenerator.getInstance().getUniqueName();
		}
	}

	public String getName() {
		return this.name;
	}
	
	public void addExpressionElement(ExpressionElementsDef expressionElement) {
		expressionElements.add(expressionElement);
	}
		
	public List<ExpressionElementsDef> getExpressionElements() {
		return this.expressionElements;
	}
	
	public void resetExpressionElements() {
		expressionElements = new ArrayList<ExpressionElementsDef>();
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer("Expression Definition : [");
		for(int index = 0; index < expressionElements.size(); index ++) {
			if(index > 0) {
				sbf.append(", ");
			}
			sbf.append(expressionElements.get(index).toString());
		}
		sbf.append("]");
		return sbf.toString();
	}
	
}
 