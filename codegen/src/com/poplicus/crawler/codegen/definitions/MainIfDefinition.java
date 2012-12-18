package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.List;

public class MainIfDefinition {
	
	private Conditions conditions = null;
	
	private List<MainIfElementsDef> ifElseElements = new ArrayList<MainIfElementsDef>();

	public Conditions getConditions() {
		return conditions;
	}

	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}

	public void setMainIfElementsDef(MainIfElementsDef ifElseElement) {
		this.ifElseElements.add(ifElseElement);
	}
	
	public List<MainIfElementsDef> getMainIfElementsDef() {
		return this.ifElseElements;
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer("Conditions : [");
		if(conditions != null) {
			List<ConditionDefinition> cons = conditions.getConditions();
			for(int conIndex = 0; conIndex < cons.size(); conIndex ++) {
				if(cons.get(conIndex) != null) {
					sbf.append(cons.get(conIndex).toString());
				} else {
					sbf.append("null");
				}
			}
		} else {
			sbf.append(conditions);
		}
		sbf.append("], IfElseElements : [");
		for(int ifElseIndex = 0; ifElseIndex < ifElseElements.size(); ifElseIndex ++) {
			sbf.append(ifElseElements.get(ifElseIndex).toString());
		}
		sbf.append("]");
		return sbf.toString();
	}
	
}
