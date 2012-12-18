package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.poplicus.crawler.codegen.LineOfCode.LOC_RHS_Type;
import com.poplicus.crawler.codegen.RHSTask.RHSTaskType;

public class ForConstruct {
	
	private Indentation indentation = new Indentation(Indentation.INDENTATION_LEVEL_3);
	
	public enum ForConstructType {
		For {
			public String toString() {
				return "for";
			}
		},
		
		ForEach {
			public String toString() {
				return "foreach";
			}
		}
	}

	private ForConstructType forConstructType = null;
	
	private List<Object> code = new ArrayList<Object>();
	
	private LineOfCode returnLOC = null;
	
	private Variable variable = null;
	
	private Condition condition = null;
	
	private String incrementDecrementOperator = "";

	public String getIncrementDecrementOperator() {
		return incrementDecrementOperator;
	}

	public void setIncrementDecrementOperator(String incrementDecrementOperator) {
		this.incrementDecrementOperator = incrementDecrementOperator;
	}

	public ForConstruct(ForConstructType forConstructType) {
		this.forConstructType = forConstructType;
	}
	
	public ForConstructType getForConstructType() {
		return forConstructType;
	}

	public void setForConstructType(ForConstructType forConstructType) {
		this.forConstructType = forConstructType;
	}
	
	public List<Object> getCode() {
		return code;
	}

	public void addCode(Object code) {
		if(code instanceof LineOfCode) {
			LineOfCode loc = (LineOfCode) code;
			loc.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			if(loc.getRHSType().equals(LOC_RHS_Type.PerformATask)) {
				if(loc.getRHSDefinition().getRhsTask().getRHSTaskType().equals(RHSTaskType.ReturnAVariable) ||
					loc.getRHSDefinition().getRhsTask().getRHSTaskType().equals(RHSTaskType.ReturnNothing)) {
					this.returnLOC = loc;
				} else {
					this.code.add(loc);
				}
			} else {
				this.code.add(loc);
			}
		} else {
			if(code instanceof StringBuffer) {
				StringBuffer tempCode = new StringBuffer(indentation.getTabsForNextLevel());
				tempCode.append(code);
				code = tempCode;
			} else if(code instanceof ForConstruct) {
				((ForConstruct)code).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(code instanceof IfElseStatement) {
				((IfElseStatement)code).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(code instanceof Variable) {
				((Variable)code).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(code instanceof Action) {
				((Action)code).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(code instanceof WhileConstruct) {
				((WhileConstruct)code).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			}
			this.code.add(code);	
		}
	}

	public StringBuffer toStringBuffer() throws RHSTaskException, LineOfCodeException, 
												ArgumentValueDefinitionException, ClassDefinitionException, 
												ActionDefinitionException {
		StringBuffer codeBuffer = new StringBuffer();

		switch (forConstructType) {
			case For:
				codeBuffer.append(Constants.NEW_LINE);
				codeBuffer.append(indentation.getTabs());
				codeBuffer.append(ForConstructType.For).append(Constants.OPEN_PARENTHESIS);
				codeBuffer.append(variable.toStringBuffer()).append(Constants.SEMI_COLON).append(Constants.SPACE);
				codeBuffer.append(condition.toStringBuffer()).append(Constants.SEMI_COLON);
				codeBuffer.append(Constants.SPACE).append(incrementDecrementOperator);
				codeBuffer.append(Constants.CLOSE_PARENTHESIS).append(Constants.NEW_LINE);
				codeBuffer.append(indentation.getTabs());
				codeBuffer.append("{");
			break;
			
			case ForEach:
			codeBuffer.append(Constants.NEW_LINE);
			codeBuffer.append(indentation.getTabs());
			codeBuffer.append(ForConstructType.ForEach).append(Constants.OPEN_PARENTHESIS);
			codeBuffer.append(condition.toStringBuffer()).append(Constants.SPACE);
			codeBuffer.append(Constants.CLOSE_PARENTHESIS).append(Constants.NEW_LINE);
			codeBuffer.append(indentation.getTabs());
			codeBuffer.append("{");
			break;				
			
			default:
			break;
		}

		Iterator<Object> itrCode = code.iterator();
		Object objCode = null;
		while(itrCode.hasNext()) {
			objCode = itrCode.next();
			if(objCode instanceof LineOfCode) {
				codeBuffer.append(Constants.NEW_LINE).append(indentation.getTabsForNextLevel());
				((LineOfCode)objCode).setStartInNewLine(false);
				codeBuffer.append(((LineOfCode)objCode).toStringBuffer());
			} else if(objCode instanceof Action) {
				codeBuffer.append(Constants.NEW_LINE);
				codeBuffer.append(indentation.getTabsForNextLevel());
				((Action)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
				codeBuffer.append(((Action)objCode).toStringBuffer());
			} else if(objCode instanceof StringBuffer) {
				codeBuffer.append(Constants.NEW_LINE).append(indentation.getTabsForNextLevel());
				codeBuffer.append(objCode);
			} else if(objCode instanceof IfElseStatement) {
				((IfElseStatement)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabs()));
				codeBuffer.append(((IfElseStatement)objCode).toStringBuffer());	
			} else if(objCode instanceof ForConstruct) {
				((ForConstruct)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
				codeBuffer.append(((ForConstruct)objCode).toStringBuffer());	
			} else if(objCode instanceof Variable) {
				((Variable)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
				codeBuffer.append(((Variable)objCode).toStringBuffer());
			} else if(objCode instanceof WhileConstruct) {
				((WhileConstruct)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
				codeBuffer.append(((WhileConstruct)objCode).toStringBuffer());
			}
		}
		
		if(forConstructType != null) {
			codeBuffer.append(Constants.NEW_LINE);
			codeBuffer.append(indentation.getTabs()).append("}");
		}
		
		return codeBuffer;
	}
	
	public void setIndentationLevel(int level) {
		indentation.setLevel(level);
		udpateIndentationOfChilds();
	}
	
	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}
	
	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public void udpateIndentationOfChilds() {
		//updating code
		Object object = null;
		int noOfCode = code.size();
		for(int index = 0 ; index < noOfCode; index ++) {
			object = code.get(index);
			if(object instanceof LineOfCode) {
				((LineOfCode)object).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(object instanceof StringBuffer) {
				StringBuffer tempCode = new StringBuffer(indentation.getTabsForNextLevel());
				tempCode.append(object);
				object = tempCode;
			} else if(object instanceof ForConstruct) {
				((ForConstruct)object).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(object instanceof IfElseStatement) {
				((IfElseStatement)object).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(object instanceof Variable) {
				((Variable)object).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(object instanceof Action) {
				((Action)object).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			}
			code.set(index, object);
		}
		
		//updating returnLOC
		if(returnLOC != null) {
			returnLOC.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
		}
	}

}
