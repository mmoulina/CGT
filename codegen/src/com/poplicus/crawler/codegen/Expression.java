package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.poplicus.crawler.codegen.LineOfCode.LOC_LHS_Type;
import com.poplicus.crawler.codegen.LineOfCode.LOC_RHS_Type;

public class Expression {
	
	public enum ExpressionOperator {
		Plus {
			public String toString() {
				return Constants.PLUS;
			}
		},
		
		Minus {
			public String toString() {
				return Constants.MINUS;
			}
		}, 
		
		Multiply {
			public String toString() {
				return Constants.MULTIPLY;
			}
		}, 
		
		Divide {
			public String toString() {
				return Constants.DIVIDE;
			}
		}, 
		
		Modulus {
			public String toString() {
				return Constants.MODULUS;
			}
		}
	}
	
	private List<Object> entities = new ArrayList<Object>();
	
	public void setOperand(Object operand) throws ExpressionException {
		Object lastEntity = null;
		if(operand != null) {
			lastEntity = this.getLastEntity();
			if(lastEntity != null) {
				if(lastEntity instanceof ExpressionOperator) {
					if(operand instanceof ExpressionOperator) {
						throw new ExpressionException("Last entity added was an ExpressionOperator. Either an Expression or an Operand can be added after ExpressionOperator.");
					} else { 
						entities.add(operand);
					}
				} else if(lastEntity instanceof Expression) {
					if(operand instanceof ExpressionOperator) {
						entities.add(operand);
					} else { 
						throw new ExpressionException("Last entity added was an Expression. Only an ExpressionOperator can be added after an Expression.");
					}
				} else { //operand
					if(operand instanceof ExpressionOperator) {
						entities.add(operand);
					} else {
						throw new ExpressionException("Last entity added was an Operator. Only an ExpressionOperator can be added after an Operator.");
					}
				}
			} else {
				if(operand instanceof ExpressionOperator) {
					throw new ExpressionException("First entity that can be added to an Expression should be either an Operand or an Expression.");
				} else {
					entities.add(operand);
				}
			}
		}
	}
	
	public void setOperator(ExpressionOperator operator) throws ExpressionException {
		Object lastEntity = null;
		if(operator != null) {
			lastEntity = this.getLastEntity();
			if(lastEntity != null) {
				if(lastEntity instanceof ExpressionOperator) {
					throw new ExpressionException("Cannot add two ExpressionOperator adjacent to each other.");
				} else {
					entities.add(operator);
				}
	 		} else {
 				throw new ExpressionException("First entity that can be added to an Expression should be either an Operand or an Expression.");	
	 		}
		}
	}
	
	private Object getLastEntity() {
		Object obj = null;
		if(entities.size() > 0) {
			obj = entities.get(entities.size() - 1);
		} 
		return obj;
	}
	
	public StringBuffer toStringBuffer() throws LineOfCodeException, ActionDefinitionException, 
												RHSTaskException, ClassDefinitionException, 
												ArgumentValueDefinitionException{
		StringBuffer code = new StringBuffer();
		int noOfEntities = entities.size(); 
		if(noOfEntities > 0) {
			Object obj = null;
			Iterator<Object> itrEntities = entities.iterator();
			while(itrEntities.hasNext()) {
				obj = itrEntities.next();
				if(obj instanceof ExpressionOperator) {
					code.append(Constants.SPACE).append(obj.toString());
				} else if(obj instanceof Expression) {
					code.append(Constants.SPACE).append(Constants.OPEN_PARENTHESIS);
					code.append(((Expression) obj).toStringBuffer()).append(Constants.CLOSE_PARENTHESIS);
				} else {
					if(obj instanceof RHSDefinition) {
						LineOfCode loc = new LineOfCode(LOC_LHS_Type.NoLHS, LOC_RHS_Type.AssignValueToAnExistingVariable, null);
						loc.setRHSDefinition((RHSDefinition)obj);
						if(code.length() == 0) {
							code.append(loc.getRHSCodeFromRHSDefinition());
						} else {
							code.append(Constants.SPACE).append(loc.getRHSCodeFromRHSDefinition());
						}
					} else if(obj instanceof Variable) { 
						if(code.length() == 0) {
							code.append(((Variable) obj).getName());
						} else {
							code.append(Constants.SPACE).append(((Variable) obj).getName());
						}
					} else if(obj instanceof Action) {
						String actionCode = ((Action) obj).toStringBuffer().toString();
						actionCode = actionCode.substring(0, actionCode.length() - 1);
						if(code.length() == 0) {
							code.append(actionCode);
						} else {
							code.append(Constants.SPACE).append(actionCode);	
						}
					} else {
						if(code.length() == 0) {
							code.append(obj);
						} else {
							code.append(Constants.SPACE).append(obj);	
						}
					}
				}
			}
		}
		return code;
	}

}
