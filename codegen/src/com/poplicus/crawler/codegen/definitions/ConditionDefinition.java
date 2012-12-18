package com.poplicus.crawler.codegen.definitions;

import com.poplicus.crawler.codegen.Condition;
import com.poplicus.crawler.codegen.factory.ConditionCache;

public class ConditionDefinition {

	private ConditionCache conditionCache = ConditionCache.getInstance();

	private String name = null;

	private boolean superCondition = false;

	private String lhsOperand = null;

	private String operator = null;

	private String rhsOperand = null;

	public ConditionDefinition() {
		this.name = ConditionNameGenerator.getInstance().getUniqueName();
	}

	public ConditionDefinition(boolean generateUniqueName) {
		if(generateUniqueName) {
			this.name = ConditionNameGenerator.getInstance().getUniqueName();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSuperCondition() {
		return superCondition;
	}

	public void setSuperCondition(boolean superCondition) {
		this.superCondition = superCondition;
	}

	public String getLhsOperand() {
		return lhsOperand;
	}

	public void setLshOperand(String lshOperand) {
		this.lhsOperand = lshOperand;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getRhsOperand() {
		return rhsOperand;
	}

	public void setRhsOperand(String rhsOperand) {
		this.rhsOperand = rhsOperand;
	}

	/**
	 * <code>getCondition()</code> method should only be invoked from CrawlerCodeGenFactory
	 * @return
	 */
	public Condition getCondition() {
		Condition con = new Condition();
		if(lhsOperand.startsWith("pop:crwl:cons:")) {
			con.setLeftSideValue(conditionCache.get(lhsOperand));
		} else {
			con.setLeftSideValue(lhsOperand);
		}
		con.setOperator(getConditionOperator(this.operator));
		if(rhsOperand.startsWith("pop:crwl:cons:")) {
			con.setRightSideValue(conditionCache.get(rhsOperand));
		} else {
			con.setRightSideValue(rhsOperand);
		}
		con.setName(name);
		con.setSuperCondition(superCondition);
		return con;
	}

	public Condition.ConditionOperator getConditionOperator(String operator) {
		Condition.ConditionOperator conOperator = null;
		if(operator.equalsIgnoreCase(Condition.ConditionOperator.Contains.toString())) {
			conOperator = Condition.ConditionOperator.Contains;
		} else if(operator.equalsIgnoreCase(Condition.ConditionOperator.DoesNotContain.toString())) {
			conOperator = Condition.ConditionOperator.DoesNotContain;
		} else if(operator.equalsIgnoreCase(Condition.ConditionOperator.LogicalAND.toString())) {
			conOperator = Condition.ConditionOperator.LogicalAND;
		} else if(operator.equalsIgnoreCase(Condition.ConditionOperator.LogicalOR.toString())) {
			conOperator = Condition.ConditionOperator.LogicalOR;
		} else if(operator.equalsIgnoreCase(Condition.ConditionOperator.Equals.toString())) {
			conOperator = Condition.ConditionOperator.Equals;
		} else if(operator.equalsIgnoreCase(Condition.ConditionOperator.NotEquals.toString())) {
			conOperator = Condition.ConditionOperator.NotEquals;
		} else if(operator.equalsIgnoreCase(Condition.ConditionOperator.LessThan.toString())) {
			conOperator = Condition.ConditionOperator.LessThan;
		} else if(operator.equalsIgnoreCase(Condition.ConditionOperator.LessThanAndEquals.toString())) {
			conOperator = Condition.ConditionOperator.LessThanAndEquals;
		} else if(operator.equalsIgnoreCase(Condition.ConditionOperator.Boolean.toString())) {
			conOperator = Condition.ConditionOperator.Boolean;
		} else if(operator.equalsIgnoreCase(Condition.ConditionOperator.LogicalComplement.toString())) {
			conOperator = Condition.ConditionOperator.LogicalComplement;
		}
		return conOperator;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer("\nCondition name : " );
		sbf.append(name).append(", superCondition : ").append(superCondition).append(", LHS Operand : ").append(lhsOperand);
		sbf.append(", Operator : ").append(operator);
		sbf.append(", RHS Operand : ").append(rhsOperand);
		return sbf.toString();
	}

}
