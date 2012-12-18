package com.poplicus.crawler.codegen;

public class Condition {
	
	public enum ConditionOperator {
		Contains {
			public String toString() {
				return "Contains";
			}
		},
		DoesNotContain {
			public String toString() {
				return "DoesNotContain";
			}
		},
		LogicalAND {
			public String toString() {
				return "LogicalAND";
			}
		},
		LogicalOR {
			public String toString() {
				return "LogicalOR";
			}
		},
		Equals {
			public String toString() {
				return "Equals";
			}
		},
		NotEquals {
			public String toString() {
				return "NotEquals";
			}
		},
		LessThan {
			public String toString() {
				return "LessThan";
			}
		},
		LessThanAndEquals {
			public String toString() {
				return "LessThanAndEquals";
			}
		},
		Boolean {
			public String toString() {
				return "Boolean";
			}
		},
		LogicalComplement {
			public String toString() {
				return "LogicalComplement";
			}
		}
	}

	//possible object types - String and Condition
	private Object leftSideValue = "primaryBrowserHTML"; // default value is primaryBrowserHTML, this is the variable that holds inner html of primary browser in crawler program
	
	private ConditionOperator operator = null;
	
	//possible object types - String and Condition
	private Object rightSideValue = null; // value could be a string or int or anything, it could even be another Condition object 
	
	private boolean superCondition = false;
	
	private String name = null;
	
	public Condition() {
		
	}
	
	public Condition(Object leftSideValue, ConditionOperator operator, Object rightSideValue) {
		if(leftSideValue != null) {
			this.leftSideValue = leftSideValue;
		}
		this.operator = operator;
		this.rightSideValue = rightSideValue;
	}

	public Condition(Object leftSideValue, ConditionOperator operator, Object rightSideValue, boolean superCondition, String name) {
		if(leftSideValue != null) {
			this.leftSideValue = leftSideValue;
		}
		this.operator = operator;
		this.rightSideValue = rightSideValue;
		this.superCondition = superCondition;
		this.name = name;
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

	public Object getLeftSideValue() {
		return leftSideValue;
	}

	public void setLeftSideValue(Object leftSideValue) {
		if(leftSideValue != null) {
			this.leftSideValue = leftSideValue;
		}
	}

	public ConditionOperator getOperator() {
		return operator;
	}

	public void setOperator(ConditionOperator operator) {
		this.operator = operator;
	}

	public Object getRightSideValue() {
		return rightSideValue;
	}

	public void setRightSideValue(Object rightSideValue) {
		this.rightSideValue = rightSideValue;
	}
	
	public StringBuffer toStringBuffer() {
		//opening the condition
		StringBuffer conditionAsText = new StringBuffer("(");
		String tempText = "";
		//adding operator
		if(operator.equals(ConditionOperator.Boolean) || operator.equals(ConditionOperator.LogicalComplement)) {
			conditionAsText = appendOperator(conditionAsText, operator);
		}
		//constructing left side value
		if(leftSideValue instanceof String) {
			conditionAsText.append(leftSideValue);
		} else if(leftSideValue instanceof Condition) {
			conditionAsText.append(((Condition)leftSideValue).toStringBuffer());
		} else if(leftSideValue instanceof Variable) {
			conditionAsText.append(((Variable)leftSideValue).getName());
		}
		//adding operator to left side value
		if(!(operator.equals(ConditionOperator.Boolean) || operator.equals(ConditionOperator.LogicalComplement))) {
			conditionAsText = appendOperator(conditionAsText, operator);
		}
		//adding right side value 
		if(rightSideValue == null) {
			rightSideValue = "";
		}
		if(rightSideValue instanceof String) {
			if(operator.equals(ConditionOperator.LogicalAND) || operator.equals(ConditionOperator.LogicalOR) 
				|| operator.equals(ConditionOperator.Equals) || operator.equals(ConditionOperator.NotEquals)
				|| operator.equals(ConditionOperator.LessThan) || operator.equals(ConditionOperator.LessThanAndEquals)) {
				conditionAsText.append(rightSideValue);
			} else if(operator.equals(ConditionOperator.Boolean) || operator.equals(ConditionOperator.LogicalComplement)) {
				//do nothing
			} else {
				//conditionAsText.append(Constants.DOUBLE_QUOTE).append(rightSideValue.toString().replace("\"", "\\\"")).append(Constants.DOUBLE_QUOTE);
				tempText = rightSideValue.toString();
				if(tempText.startsWith("\"") && tempText.endsWith("\"")) {
					tempText = tempText.substring(1, tempText.length() - 1);
					tempText = tempText.replace("\"", "\\\"");
					tempText = "\"" + tempText + "\"";
				}
				conditionAsText.append(tempText);
			}
		} else if(rightSideValue instanceof Condition) {
			conditionAsText.append(((Condition)rightSideValue).toStringBuffer());
		} else if(rightSideValue instanceof Variable) {
			conditionAsText.append(((Variable)rightSideValue).getName());
		}
		//closing the operator
		conditionAsText.append(getCloser(operator));
		//closing the condition
		conditionAsText.append(")");
		return conditionAsText;
	}
	
	private StringBuffer appendOperator(StringBuffer leftSideValue, ConditionOperator operator) {
		switch (operator) {
		
			case Contains:
				leftSideValue.append(Constants.DOT).append(Constants.CONTAINS).append(Constants.OPEN_PARENTHESIS);
			break;
			
			case DoesNotContain:
				StringBuffer tempStringBuffer = new StringBuffer();
				tempStringBuffer.append(Constants.OPEN_PARENTHESIS).append(Constants.LOGICAL_COMPLEMENT).append(leftSideValue);
				tempStringBuffer.append(Constants.DOT).append(Constants.CONTAINS).append(Constants.OPEN_PARENTHESIS);
				leftSideValue = tempStringBuffer;
			break;
			
			case LogicalAND:
				leftSideValue.append(Constants.SPACE).append(Constants.LOGICAL_AND).append(Constants.SPACE);
			break;
			
			case LogicalOR:
				leftSideValue.append(Constants.SPACE).append(Constants.LOGICAL_OR).append(Constants.SPACE);
			break;
			
			case Equals:
				leftSideValue.append(Constants.SPACE).append(Constants.EQUALS_CONDITION).append(Constants.SPACE);
			break;
			
			case NotEquals:
				leftSideValue.append(Constants.SPACE).append(Constants.NOT_EQUALS_CONDITION).append(Constants.SPACE);
			break;
			
			case LessThan:
				leftSideValue.append(Constants.SPACE).append(Constants.LESS_THAN).append(Constants.SPACE);
			break;
			
			case LessThanAndEquals:
				leftSideValue.append(Constants.SPACE).append(Constants.LESS_THAN_AND_EQUALS).append(Constants.SPACE);
			break;
			
			case Boolean:
				//do nothing
			break;
			
			case LogicalComplement:
				leftSideValue.append(Constants.LOGICAL_COMPLEMENT);
			break;
			
			default:
				// do nothing
			break;
		
		}
		return leftSideValue;
	}
	
	private StringBuffer getCloser(ConditionOperator operator) {
		StringBuffer closer = new StringBuffer();
		switch (operator) {
		
			case Contains:
				closer.append(Constants.CLOSE_PARENTHESIS);
			break;
			
			case DoesNotContain:
				closer.append(Constants.CLOSE_PARENTHESIS).append(Constants.CLOSE_PARENTHESIS);
			break;
			
			default:
				// do nothing
			break;
	
		}
		return closer;
	}
	
}
