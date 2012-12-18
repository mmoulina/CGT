package com.poplicus.crawler.codegen;

import java.util.List;

import com.poplicus.crawler.codegen.ArgumentValueDefinition.ArgumentValueType;

/**
 * User should be able to add any line of code using this class. 
 * Scope of this class will be expanded as and when required.
 * 
 * @author mmoulina
 *
 */
public class LineOfCode {
	
	private Indentation indentation = new Indentation(Indentation.INDENTATION_LEVEL_4);
	
	public enum LOC_LHS_Type {
		DeclareInstanceVariableAndInitialize {
			public String toString() {
				return "DeclareInstanceVariableAndInitialize";
			}
		},
		DeclareMethodVariableAndInitialize {
			public String toString() {
				return "DeclareMethodVariableAndInitialize";
			}
		},
		DeclareInstanceVariable {
			public String toString() {
				return "DeclareInstanceVariable";
			}
		},
		DeclareMethodVariable {
			public String toString() {
				return "DeclareMethodVariable";
			}
		},
		AssignValueToAnExistingVariable {
			public String toString() {
				return "AssignValueToAnExistingVariable";
			}
		},
		NoLHS {
			public String toString() {
				return "NoLHS";
			}
		}
	}
	
	public enum LOC_RHS_Type {
		AssignValueToAnExistingVariable {
			public String toString() {
				return "AssignValueToAnExistingVariable";
			}
		},
		PerformATask {
			public String toString() {
				return "PerformATask";
			}
		},
		ReturnValueFromAction {
			public String toString() {
				return "ReturnValueFromAction";
			}
		},
		Expression {
			public String toString() {
				return "Expression";
			}
		},
		NoRHS {
			public String toString() {
				return "NoRHS";
			}
		}
	}
	
	private StringBuffer leftHandSide = new StringBuffer();
	private StringBuffer rightHandSide = new StringBuffer();
	
	private LOC_LHS_Type lhsType = null;
	private LOC_RHS_Type rhsType = null;
	
	public Class containingClass = null;
	
	private Variable lhsVariable = null;
	
	private RHSDefinition rhsDefinition = null;
	
	private Action rhsAction = null;
	
	private Expression rhsExpression = null;
	
	private boolean startInNewLine = true;
	
	public LineOfCode(LOC_LHS_Type lhsType, LOC_RHS_Type rhsType, Class containingClass) throws LineOfCodeException{
		if(lhsType == null) {
			throw new LineOfCodeException("LHS Type is null.");
		}
		if(rhsType == null) {
			throw new LineOfCodeException("RHS Type is null.");
		}
		this.lhsType = lhsType;
		this.rhsType = rhsType;
		this.containingClass = containingClass;
	}
	
	public LineOfCode(LOC_LHS_Type lhsType, LOC_RHS_Type rhsType) throws LineOfCodeException{
		if(lhsType == null) {
			throw new LineOfCodeException("LHS Type is null.");
		}
		if(rhsType == null) {
			throw new LineOfCodeException("RHS Type is null.");
		}
		this.lhsType = lhsType;
		this.rhsType = rhsType;
	}

	
	public void setLHSVariable(Variable lhsVariable) {
		this.lhsVariable = lhsVariable;
	}
	
	public void setRHSDefinition(RHSDefinition rhsDef) {
		this.rhsDefinition = rhsDef;
	}
	
	public void setRHSDefinition(Action action) {
		this.rhsAction = action;
	}
	
	public void setRHSDefinition(Expression expression) {
		this.rhsExpression = expression;
	}
	
	public String getLHSVariableName() throws LineOfCodeException {
		if(lhsType.equals(LOC_LHS_Type.NoLHS)) {
			return null;
		} else {
			if(lhsVariable != null) {
				return this.lhsVariable.getName();	
			} else {
				throw new LineOfCodeException("LHS variable not defined in RHSDefinition.");
			}
		}
	}
	
	public StringBuffer toStringBuffer() throws ClassDefinitionException, ArgumentValueDefinitionException, 
												LineOfCodeException, RHSTaskException, ActionDefinitionException {
		leftHandSide = new StringBuffer();
		rightHandSide = new StringBuffer();
		if(! lhsType.equals(LOC_LHS_Type.NoLHS)) {
			leftHandSide = getLHSCode();
		}
		if(!rhsType.equals(LOC_RHS_Type.NoRHS)) {
			rightHandSide = getRHSCode();
			leftHandSide.append(rightHandSide);
			if(rhsType.equals(LOC_RHS_Type.AssignValueToAnExistingVariable) 
				|| rhsType.equals(LOC_RHS_Type.Expression)) {
				leftHandSide.append(Constants.SEMI_COLON);
			}
//			if(! rhsType.equals(LOC_RHS_Type.ReturnValueFromAction) ) {
//				leftHandSide.append(Constants.SEMI_COLON);
//			}
		}
		return leftHandSide;
	}
	
	public StringBuffer getRHSCode() throws LineOfCodeException, ArgumentValueDefinitionException, 
											ClassDefinitionException, RHSTaskException, ActionDefinitionException {
		StringBuffer rightHandSide = new StringBuffer();
		if(rhsType.equals(LOC_RHS_Type.AssignValueToAnExistingVariable)) {
			rightHandSide.append(getCodeForAssigningValueToAnExistingVariable());// This is the actual code, commented after making changes to the way Action should work
			//based on the UI design.
			//rightHandSide.append(rhsAction.toStringBuffer());
		} else if(rhsType.equals(LOC_RHS_Type.PerformATask)) {
			rightHandSide.append(rhsDefinition.getRhsTask().toStringBuffer());
		} else if(rhsType.equals(LOC_RHS_Type.ReturnValueFromAction)) {
			//rightHandSide.append(Constants.SPACE).append(rhsAction.toStringBuffer());
			rightHandSide.append(rhsAction.toStringBuffer());
		} else if(rhsType.equals(LOC_RHS_Type.Expression)) {
			//rightHandSide.append(Constants.SPACE).append(rhsExpression.toStringBuffer());
			rightHandSide.append(rhsExpression.toStringBuffer());
		} else if(rhsType.equals(LOC_RHS_Type.NoRHS)) {
			//do nothing
		}
		return rightHandSide;
	}
	
	public StringBuffer getRHSCodeFromRHSDefinition() throws LineOfCodeException, ArgumentValueDefinitionException, 
															 ClassDefinitionException, RHSTaskException, ActionDefinitionException {
		StringBuffer rightHandSide = new StringBuffer();
		if(rhsType.equals(LOC_RHS_Type.AssignValueToAnExistingVariable)) {
			rightHandSide.append(getCodeForAssigningValueToAnExistingVariable());
		} else if(rhsType.equals(LOC_RHS_Type.PerformATask)) {
			rightHandSide.append(rhsDefinition.getRhsTask().toStringBuffer());
		}
		return rightHandSide;
	}
	
	private StringBuffer getLHSCode() throws LineOfCodeException {
		StringBuffer code = new StringBuffer();
		if(lhsVariable != null) {
//			if(containingClass == null) {
//				throw new LineOfCodeException("Containing class is null.");
//			}
			
			if(lhsType.equals(LOC_LHS_Type.DeclareInstanceVariable) || lhsType.equals(LOC_LHS_Type.DeclareInstanceVariableAndInitialize)) {
				if(containingClass != null) {
					if(rhsType.equals(LOC_RHS_Type.NoRHS)) {
						lhsVariable.setNoRHS(true);
					}
					this.containingClass.addInstanceVariable(lhsVariable);
				} else {
					if(lhsType.equals(LOC_LHS_Type.DeclareInstanceVariable)) {
						if(rhsType.equals(LOC_RHS_Type.NoRHS)) {
							lhsVariable.setNoRHS(true);
							code.append("//declared an instance variable, " + lhsVariable.getName() + ".");
						}
					}
				}
				if(lhsType.equals(LOC_LHS_Type.DeclareInstanceVariableAndInitialize)) {
					code.append(lhsVariable.getName()).append(Constants.SPACE).append(Constants.EQUALS).append(Constants.SPACE);
					if(rhsType.equals(LOC_RHS_Type.NoRHS)) {
						code.append(lhsVariable.getValue()).append(Constants.SEMI_COLON);
					}
				}
			}
			
			if(lhsType.equals(LOC_LHS_Type.DeclareMethodVariableAndInitialize) || lhsType.equals(LOC_LHS_Type.DeclareMethodVariable)) {
				if(isStartInNewLine()) {
					code.append(Constants.NEW_LINE).append(indentation.getTabs()).append(lhsVariable.getType());	
				} else {
					code.append(lhsVariable.getType());
				}
				code.append(Constants.SPACE).append(lhsVariable.getName());
				if(lhsType.equals(LOC_LHS_Type.DeclareMethodVariableAndInitialize)) {
					if(rhsType.equals(LOC_RHS_Type.NoRHS)) {
						code.append(Constants.SPACE).append(Constants.EQUALS).append(Constants.SPACE);
						code.append(lhsVariable.getValue()).append(Constants.SEMI_COLON);
					} else {
						code.append(Constants.SPACE).append(Constants.EQUALS).append(Constants.SPACE);
					}
				} else if(lhsType.equals(LOC_LHS_Type.DeclareMethodVariable)) {
					code.append(Constants.SEMI_COLON);
				}
			}
			if(lhsType.equals(LOC_LHS_Type.AssignValueToAnExistingVariable)) {
				if(isStartInNewLine()) {
					code.append(Constants.NEW_LINE).append(indentation.getTabs()).append(lhsVariable.getName()).append(Constants.SPACE);	
				} else {
					code.append(lhsVariable.getName()).append(Constants.SPACE);
				}
				code.append(Constants.EQUALS).append(Constants.SPACE);
				if(rhsType.equals(LOC_RHS_Type.NoRHS)) {
					code.append(lhsVariable.getValue()).append(Constants.SEMI_COLON);
				}
			}
		} else {
			throw new LineOfCodeException("LHS variable is null.");
		}
		return code;
	}
	
	private StringBuffer getCodeForAssigningValueToAnExistingVariable() throws ClassDefinitionException, ArgumentValueDefinitionException, LineOfCodeException {
		StringBuffer code = new StringBuffer();
		if(rhsDefinition != null) {
			ClassDefinition classDef = ClassDefinitions.getClassDefinition(rhsDefinition.getPackageName(), 
				rhsDefinition.getClassName(), rhsDefinition.getFullyQualifiedClassName());
			MethodDefinition methodDef = ClassDefinitions.getMethodDefinition(rhsDefinition.getPackageName(), 
				rhsDefinition.getClassName(), rhsDefinition.getFullyQualifiedClassName(), rhsDefinition.getMethodNameKey());
			List<ArgumentDefinition> masterArgDefs = methodDef.getArgumentDefinitions();

			int noOfArgs = masterArgDefs.size();
			if(rhsDefinition.getArgumentValueDefinitions().size() != noOfArgs) {
				throw new ArgumentValueDefinitionException("Number of arguments passed doesn't match number of arguments defined for " 
						+ rhsDefinition.getPackageName() + Constants.DOT + rhsDefinition.getClassName() + Constants.DOT 
						+ rhsDefinition.getMethodNameKey() + Constants.DOT);
			}
			
			//className.methodName( is created here
			if(methodDef.isStaticAccess()) {
				code.append(classDef.getClassName());
			} else {
				code.append(classDef.getVariableRefName());
			}
			
			code.append(Constants.DOT).append(methodDef.getMethodName()).append(Constants.OPEN_PARENTHESIS);
			
			//adding arguments to method
			getArgumentString(code, masterArgDefs, noOfArgs, false);
			code.append(Constants.CLOSE_PARENTHESIS);
			
			if(rhsDefinition.getSupportingMethodName() != null) {
				MethodDefinition supportingMethodDef = methodDef.getSupportingMethodDefinition(rhsDefinition.getSupportingMethodName());
				List<ArgumentDefinition> smArgDefs = supportingMethodDef.getArgumentDefinitions();
				int noOfSMArgs = smArgDefs.size();
				code.append(Constants.DOT).append(supportingMethodDef.getMethodName()).append(Constants.OPEN_PARENTHESIS);
				getArgumentString(code, smArgDefs, noOfSMArgs, true);
				code.append(Constants.CLOSE_PARENTHESIS);
			}
		}
		return code;
	}

	private void getArgumentString(StringBuffer code, List<ArgumentDefinition> masterArgDefs, 
								   int noOfArgs, boolean supportingMethod) throws ArgumentValueDefinitionException {
		ArgumentDefinition argDef = null;
		ArgumentValueDefinition argValDef = null;
		for(int argIndex = 0; argIndex < noOfArgs; argIndex ++) {
			if(argIndex > 0) {
				code.append(Constants.COMMA).append(Constants.SPACE);
			}
			argDef = masterArgDefs.get(argIndex);
			
			if(supportingMethod) {
				argValDef = rhsDefinition.getSupportingMethodArgumentValueDefinition(argDef.getArgumentName());
			} else {
				argValDef = rhsDefinition.getArgumentValueDefinition(argDef.getArgumentName());
			}
			if(argValDef == null) {
				throw new ArgumentValueDefinitionException("One or more required ArgumentValueDefinitions are missing in RHSDefinition.");
			}
			
			if(argValDef.getArgValueType().equals(ArgumentValueType.PassByReference)) {
				code.append(argValDef.getValue());
			} else if(argValDef.getArgValueType().equals(ArgumentValueType.PassByValue)) {
				code.append(Constants.DOUBLE_QUOTE).append(argValDef.getValue()).append(Constants.DOUBLE_QUOTE);
			}
		}
	}
	
	public void setIndentationLevel(int indentationLevel) {
		this.indentation.setLevel(indentationLevel);
	}
	
	public boolean isStartInNewLine() {
		return startInNewLine;
	}

	public void setStartInNewLine(boolean startInNewLine) {
		this.startInNewLine = startInNewLine;
	}
	
	public LOC_LHS_Type getLHSType() {
		return lhsType;
	}
	
	public LOC_RHS_Type getRHSType() {
		return rhsType;
	}
	
	public RHSDefinition getRHSDefinition() {
		return this.rhsDefinition;
	}

}
