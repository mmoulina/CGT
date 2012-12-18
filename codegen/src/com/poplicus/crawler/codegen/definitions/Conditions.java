package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Conditions implements Iterable<ConditionDefinition> {

	private List<ConditionDefinition> conditions = new ArrayList<ConditionDefinition>();

	public Conditions() {

	}

	public Conditions(List<ConditionDefinition> conditions) {
		this.conditions = conditions;
	}

	public List<ConditionDefinition> getConditions() {
		return conditions;
	}

	public void setConditions(List<ConditionDefinition> conditions) {
		this.conditions = conditions;
	}

	public void addCondition(ConditionDefinition condition) {
		this.conditions.add(condition);
	}

	public int size() {
		return conditions.size();
	}

	@Override
	public Iterator<ConditionDefinition> iterator() {
		return conditions.iterator();
	}

}
