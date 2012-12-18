package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.poplicus.crawler.codegen.LineOfCode.LOC_RHS_Type;
import com.poplicus.crawler.codegen.RHSTask.RHSTaskType;

public class IfElseStatement {

	protected Indentation indentation = new Indentation(Indentation.INDENTATION_LEVEL_3);

	public enum IfElseStatementType {
		If {
			public String toString() {
				return "if";
			}
		},
		ElseIf {
			public String toString() {
				return "else if";
			}
		},
		Else {
			public String toString() {
				return "else";
			}
		},
		None
	}

	protected IfElseStatementType statementType = null;

	protected Condition condition = null;

	private List<Object> code = new ArrayList<Object>();

	private List<WebPage> webPages = new ArrayList<WebPage>();

	private LineOfCode returnLOC = null;

	protected List<IfElseStatement> peerIfElseStatements = new ArrayList<IfElseStatement>();

	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IfElseStatement> getPeerIfElseStatements() {
		return peerIfElseStatements;
	}

	public void setPeerIfElseStatement(IfElseStatement ifElseStatement) throws IfElseStatementException {
		if(!statementType.equals(IfElseStatementType.If)) {
			throw new IfElseStatementException("Cannot add IfElseStatement as peer to another IfElseStatement that has "
				+ "one of the following IfElseStatementType(s): ElseIf, Else, None.");
		}
		if(ifElseStatement.getStatementType().equals(IfElseStatementType.If)) {
			throw new IfElseStatementException("Cannot add IfElseStatement with If as IfElseStatmentType to a peer IfElseStatement "
				+ "with IfElseStatementType as If.");
		}
		ifElseStatement.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabs()));
		this.peerIfElseStatements.add(ifElseStatement);
	}

	public void setIndentationLevel(int indentationLevel) {
		this.indentation.setLevel(indentationLevel);
		this.udpateIndentationOfChilds();
		this.updateIndentationOfPeers();
	}

	public Indentation getIndentation() {
		return indentation;
	}

	public List<WebPage> getWebPages() {
		return webPages;
	}

	public void setWebPages(List<WebPage> webPages) {
		if(webPages != null) {
			Iterator<WebPage> itrWebPages = webPages.iterator();
			WebPage webPage = null;
			while(itrWebPages.hasNext()) {
				webPage = itrWebPages.next();
				if(this.statementType.equals(IfElseStatementType.None)) {
					webPage.setIndentationLevel(indentation.getLevel());
				} else {
					webPage.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
				}
				this.webPages.add(webPage);
			}
		}
	}

	public IfElseStatement(IfElseStatementType statementType) {
		this.statementType = statementType;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public List<Object> getCode() {
		return code;
	}

	public void setCode(List<Object> code) {
		this.code = code;
	}

	public void addCode(Object code) {
		if(code instanceof LineOfCode) {
			LineOfCode loc = (LineOfCode) code;
			loc.setIndentationLevel(indentation.getLevel());
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
				StringBuffer tempCode = null;
				tempCode = new StringBuffer(indentation.getLevel());
				tempCode.append(code);
				code = tempCode;
			} else if(code instanceof ForConstruct) {
				((ForConstruct)code).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(code instanceof WhileConstruct) {
				((WhileConstruct)code).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
		    }else if(code instanceof IfElseStatement) {
				((IfElseStatement)code).setIndentationLevel(indentation.getLevel());
			} else if(code instanceof Variable) {
				((Variable)code).setIndentationLevel(indentation.getLevel());
			} else if(code instanceof Action) {
				((Action)code).setIndentationLevel(indentation.getLevel());
			}
			this.code.add(code);
		}
	}

	public IfElseStatementType getStatementType() {
		return statementType;
	}

	public void setStatementType(IfElseStatementType statementType) {
		this.statementType = statementType;
	}

	public StringBuffer toStringBuffer() throws RHSTaskException, LineOfCodeException,
												ArgumentValueDefinitionException, ClassDefinitionException,
												ActionDefinitionException {
		StringBuffer codeBuffer = new StringBuffer();

		switch (statementType) {
			case If:
				codeBuffer.append(Constants.NEW_LINE);
				codeBuffer.append(indentation.getTabsForNextLevel());//codeBuffer.append("<<" + indentation.getNumberOfTabs(indentation.getTabsForNextLevel()) + ">>");
				codeBuffer.append(IfElseStatementType.If).append(condition.toStringBuffer()).append(Constants.NEW_LINE);
				codeBuffer.append(indentation.getTabsForNextLevel());
				codeBuffer.append("{");
			break;

			case ElseIf:
				codeBuffer.append(Constants.NEW_LINE);
				codeBuffer.append(indentation.getTabsForNextLevel());//codeBuffer.append("<<" + indentation.getNumberOfTabs(indentation.getTabsForNextLevel()) + ">>");
				codeBuffer.append(IfElseStatementType.ElseIf).append(condition.toStringBuffer()).append(Constants.NEW_LINE);
				codeBuffer.append(indentation.getTabsForNextLevel());
				codeBuffer.append("{");
			break;

			case Else:
				codeBuffer.append(Constants.NEW_LINE);
				codeBuffer.append(indentation.getTabsForNextLevel());//codeBuffer.append("<<" + indentation.getNumberOfTabs(indentation.getTabsForNextLevel()) + ">>");
				codeBuffer.append(IfElseStatementType.Else).append(Constants.NEW_LINE);
				codeBuffer.append(indentation.getTabsForNextLevel());
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
				codeBuffer.append(Constants.NEW_LINE);
				if(statementType.equals(IfElseStatementType.None)) {
					//codeBuffer.append(indentation.getTabsForNextLevel() + "<<" + indentation.getNumberOfTabs(indentation.getTabsForNextLevel()) + ">>" + "None..");
					codeBuffer.append(indentation.getTabsForNextLevel());
				} else {
					//codeBuffer.append(indentation.getAdditionalTabs(2) + "Not None..");
					codeBuffer.append(indentation.getAdditionalTabs(2));
				}
				((LineOfCode)objCode).setStartInNewLine(false);
				//codeBuffer.append("<<LoC>>" + ((LineOfCode)objCode).toStringBuffer());
				codeBuffer.append(((LineOfCode)objCode).toStringBuffer());
			} else if(objCode instanceof Action) {
				codeBuffer.append(Constants.NEW_LINE);
				if(statementType.equals(IfElseStatementType.None)) {
					//codeBuffer.append(indentation.getTabsForNextLevel() + "<<" + indentation.getNumberOfTabs(indentation.getTabsForNextLevel()) + ">>" + "None**..");
					codeBuffer.append(indentation.getTabsForNextLevel());
					//.. below is needed for NextAction();
					((Action)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
				} else {
					//codeBuffer.append(indentation.getAdditionalTabs(2) + "Not None..");
					codeBuffer.append(indentation.getAdditionalTabs(2));
					//.. below is needed for NextAction();
					((Action)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()) + 1);
				}
				//codeBuffer.append("Action.." + ((Action)objCode).toStringBuffer());
				codeBuffer.append(((Action)objCode).toStringBuffer());
			} else if(objCode instanceof StringBuffer) {
				codeBuffer.append(Constants.NEW_LINE);//.append(indentation.getTabs());
				if(statementType.equals(IfElseStatementType.None)) {
					codeBuffer.append(indentation.getTabsForNextLevel());
				} else {
					codeBuffer.append(indentation.getAdditionalTabs(2));
				}
				//codeBuffer.append("SB.." + objCode);
				codeBuffer.append(objCode);
			} else if(objCode instanceof ForConstruct) {
				//codeBuffer.append(Constants.NEW_LINE)
				codeBuffer.append(indentation.getTabs());
				if(statementType.equals(IfElseStatementType.None)) {
					codeBuffer.append(indentation.getTabs());
				} else {
					codeBuffer.append(indentation.getAdditionalTabs(2));
					((ForConstruct)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getAdditionalTabs(2)));
				}
				codeBuffer.append(((ForConstruct)objCode).toStringBuffer());
			} else if(objCode instanceof WhileConstruct) {
				//codeBuffer.append(Constants.NEW_LINE)
				codeBuffer.append(indentation.getTabs());
				if(statementType.equals(IfElseStatementType.None)) {
					codeBuffer.append(indentation.getTabs());
				} else {
					codeBuffer.append(indentation.getAdditionalTabs(2));
					((WhileConstruct)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getAdditionalTabs(2)));
				}
				codeBuffer.append(((WhileConstruct)objCode).toStringBuffer());
			}else if(objCode instanceof IfElseStatement) {
				if(statementType.equals(IfElseStatementType.None)) {
					codeBuffer.append(indentation.getTabs());
					((IfElseStatement)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabs()));
				} else {
					codeBuffer.append(indentation.getTabsForNextLevel());
					((IfElseStatement)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
				}
				codeBuffer.append(((IfElseStatement)objCode).toStringBuffer());
			} else if(objCode instanceof Variable) {
				codeBuffer.append(indentation.getTabsForNextLevel());
				((Variable)objCode).setIndentationLevel(indentation.getNumberOfTabs(indentation.getAdditionalTabs(2)));
				codeBuffer.append(((Variable)objCode).toStringBuffer());
			}
		}

		if(returnLOC != null) {
			codeBuffer.append(Constants.NEW_LINE);
			if(statementType.equals(IfElseStatementType.None)) {
			codeBuffer.append(indentation.getTabsForNextLevel());
				returnLOC.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else {
				codeBuffer.append(indentation.getAdditionalTabs(2));
				returnLOC.setIndentationLevel(indentation.getNumberOfTabs(indentation.getAdditionalTabs(2)));
			}
			//codeBuffer.append(indentation.getTabsForNextLevel() + "<<" + indentation.getNumberOfTabs(indentation.getTabsForNextLevel()) + ">>");
			//System.out.println("<<returnLOC Code..>>" + returnLOC.toStringBuffer());
			codeBuffer.append(returnLOC.toStringBuffer());
		}

		if(!statementType.equals(IfElseStatementType.None)) {
			codeBuffer.append(Constants.NEW_LINE);
			codeBuffer.append(indentation.getTabsForNextLevel()).append("}");
		}

		for(IfElseStatement ifElseStatement : peerIfElseStatements) {
			if(ifElseStatement != null) {
				ifElseStatement.setIndentationLevel(indentation.getLevel());
				codeBuffer.append(ifElseStatement.toStringBuffer());
			}
		}

		return codeBuffer;
	}

	public StringBuffer toStringBuffer2() {
		StringBuffer code = new StringBuffer();
		switch (statementType) {
			case If:
				code.append(IfElseStatementType.If).append(condition.toStringBuffer());
				code.append(Constants.NEW_LINE).append(indentation.getTabs()).append(Constants.OPEN_BRACES);
			break;

			case ElseIf:
				code.append(IfElseStatementType.ElseIf).append(condition.toStringBuffer());
				code.append(Constants.NEW_LINE).append(indentation.getTabs()).append(Constants.OPEN_BRACES);
			break;

			case Else:
				code.append(IfElseStatementType.Else).append(condition.toStringBuffer());
				code.append(Constants.NEW_LINE).append(indentation.getTabs()).append(Constants.OPEN_BRACES);
			break;

			default:
			break;
		}
		return code;
	}

	public void udpateIndentationOfChilds() {
		//updating code
		Object object = null;
		int noOfCode = code.size();
		for(int index = 0; index < noOfCode; index ++) {
			object = code.get(index);
			if(object instanceof LineOfCode) {
				((LineOfCode)object).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(object instanceof StringBuffer) {
				StringBuffer tempCode = new StringBuffer(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
				tempCode.append(object);
				object = tempCode;
			} else if(object instanceof Action) {
				((Action)object).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(object instanceof IfElseStatement) {
				((IfElseStatement)object).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(object instanceof ForConstruct) {
				((ForConstruct)object).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			} else if(object instanceof Variable) {
				((Variable)object).setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			}
			code.set(index, object);
		}

		//updating webPages
		WebPage webPage = null;
		int noOfWebPages = webPages.size();
		for(int index = 0; index < noOfWebPages; index ++) {
			webPage = webPages.get(index);
			webPage.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			webPages.set(index, webPage);
		}

		//updating return LOC
		if(returnLOC != null) {
			if(statementType.equals(IfElseStatementType.None)) {
				returnLOC.setIndentationLevel(indentation.getLevel());
			} else {
				//returnLOC.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
				returnLOC.setIndentationLevel(indentation.getNumberOfTabs(indentation.getAdditionalTabs(2)));
			}
		}
	}

	public void updateIndentationOfPeers() {
		//updating peerIfElseStatements
		IfElseStatement ifElse = null;
		int noOfPeers = peerIfElseStatements.size();
		for(int peerIndex = 0; peerIndex < noOfPeers; peerIndex ++) {
			ifElse = peerIfElseStatements.get(peerIndex);
			ifElse.setIndentationLevel(indentation.getLevel());
			peerIfElseStatements.set(peerIndex, ifElse);
		}
	}

}
