package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebPageDefinition {

	private String uniqueName = null;

	private String name = null;

	private String type = null;

	private String navigationOrder = null;

	private boolean executeOnce = false;

	private String browser = null;

	private String url = null;

	private Conditions conditions = null;

	private MainIfDefinition mainIF = null;

	private List<DataGroupDefinition> dataGroups = new ArrayList<DataGroupDefinition>();

	public WebPageDefinition() {
		this.uniqueName = WebPageNameGenerator.getInstance().getUniqueName();
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNavigationOrder() {
		return navigationOrder;
	}

	public void setNavigationOrder(String navigationOrder) {
		this.navigationOrder = navigationOrder;
	}

	public boolean isExecuteOnce() {
		return executeOnce;
	}

	public void setExecuteOnce(boolean executeOnce) {
		this.executeOnce = executeOnce;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public Conditions getConditions() {
		return conditions;
	}

	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}

	public MainIfDefinition getMainIF() {
		return mainIF;
	}

	public void setMainIF(MainIfDefinition mainIF) {
		this.mainIF = mainIF;
	}

	public void addDataGroup(DataGroupDefinition dataGroup) {
		this.dataGroups.add(dataGroup);
	}

	public void replaceDataGroup(DataGroupDefinition dataGroup) {
		this.dataGroups = new ArrayList<DataGroupDefinition>();
		this.dataGroups.add(dataGroup);
	}

	public List<DataGroupDefinition> getDataGroups() {
		return this.dataGroups;
	}

	public boolean isValidWebPageDefinition() {
		boolean valid = true;
		if(getType().equalsIgnoreCase("ResultLinks")) {
			valid = validateLineOfCodes("CollectResultLinks");
		} else if(getType().equalsIgnoreCase("Result")) {
			valid = validateLineOfCodes("ParseResult");
		}
		return valid;
	}

	private boolean validateLineOfCodes(String actionType) {
		boolean flag = false;
		List<MainIfElementsDef> mainIfElems = mainIF.getMainIfElementsDef();
		int noOfMainElements = mainIfElems.size();
		for(int mElemIndex = 0; mElemIndex < noOfMainElements; mElemIndex ++) {
			if(mainIfElems.get(mElemIndex) instanceof ActionDefinition) {
				if(((ActionDefinition) mainIfElems.get(mElemIndex)).getType().equalsIgnoreCase(actionType)) {
					flag = true;
					break;
				}
			} else {
				List<IfElseElementsDef> ifElems = null;
				if(mainIfElems.get(mElemIndex) instanceof IfDefinition) {
					ifElems = ((IfDefinition) mainIfElems.get(mElemIndex)).getIfElseElements();
				} else if(mainIfElems.get(mElemIndex) instanceof ElseIfDefinition) {
					ifElems = ((ElseIfDefinition) mainIfElems.get(mElemIndex)).getIfElseElements();
				} else if(mainIfElems.get(mElemIndex) instanceof ElseDefinition) {
					ifElems = ((ElseDefinition) mainIfElems.get(mElemIndex)).getIfElseElements();
				}
				if(ifElems != null) {
					int noOfIfChilds = ifElems.size();
					for(int ifIndex = 0; ifIndex < noOfIfChilds; ifIndex ++) {
						if(ifElems.get(ifIndex) instanceof ActionDefinition) {
							if(((ActionDefinition) ifElems.get(ifIndex)).getType().equalsIgnoreCase(actionType)) {
								flag = true;
								break;
							}
						} else if(ifElems.get(ifIndex) instanceof IfDefinition
							|| ifElems.get(ifIndex) instanceof ElseIfDefinition
							|| ifElems.get(ifIndex) instanceof ElseDefinition) {
							flag = validateLineOfCodes(ifElems.get(ifIndex), actionType);
							if(flag) {
								break;
							}
						}
					}
					if(flag) {
						break;
					}
				}
			}
		}
		return flag;
	}

	private boolean validateLineOfCodes(IfElseElementsDef ifElseElement, String actionType) {
		boolean valid = false;
		List<IfElseElementsDef> childElems = null;
		if(ifElseElement instanceof IfDefinition) {
			childElems = ((IfDefinition) ifElseElement).getIfElseElements();
		} else if(ifElseElement instanceof ElseIfDefinition) {
			childElems = ((ElseIfDefinition) ifElseElement).getIfElseElements();
		} else if(ifElseElement instanceof ElseDefinition) {
			childElems = ((ElseDefinition) ifElseElement).getIfElseElements();
		}
		if(childElems != null) {
			int noOfChilds = childElems.size();
			for(int childIndex = 0; childIndex < noOfChilds; childIndex ++) {
				if(childElems.get(childIndex) instanceof ActionDefinition) {
					if(((ActionDefinition) childElems.get(childIndex)).getType().equalsIgnoreCase(actionType)) {
						valid = true;
						break;
					}
				} else if(childElems.get(childIndex) instanceof IfDefinition
						|| childElems.get(childIndex) instanceof ElseIfDefinition
						|| childElems.get(childIndex) instanceof ElseDefinition) {
					valid = validateLineOfCodes(childElems.get(childIndex), actionType);
					if(valid) {
						break;
					}
				}
			}
		}
		return valid;
	}

	public void addIfElseElementsDef(IfElseElementsDef ifElseElementsDef) {
		UUID parentNodeID = getParentNodeID(ifElseElementsDef);
		IfElseElementsDef parentNode = getElementByNodeID_Level_One(parentNodeID);
		if(parentNode instanceof IfDefinition) {
			((IfDefinition) parentNode).setIfElseElement(ifElseElementsDef);
		} else if(parentNode instanceof ElseIfDefinition) {
			((ElseIfDefinition) parentNode).setIfElseElement(ifElseElementsDef);
		} else if(parentNode instanceof ElseDefinition) {
			((ElseDefinition) parentNode).setIfElseElement(ifElseElementsDef);
		}
	}

	public UUID getParentNodeID(IfElseElementsDef ifElseElementsDef) {
		UUID parentNodeID = null;
		if(ifElseElementsDef instanceof IfDefinition) {
			parentNodeID = ((IfDefinition) ifElseElementsDef).getParentNodeID();
		} else if(ifElseElementsDef instanceof ElseIfDefinition) {
			parentNodeID = ((ElseIfDefinition) ifElseElementsDef).getParentNodeID();
		} else if(ifElseElementsDef instanceof ElseDefinition) {
			parentNodeID = ((ElseDefinition) ifElseElementsDef).getParentNodeID();
		} else if(ifElseElementsDef instanceof LineOfCodeDefinition) {
			parentNodeID = ((LineOfCodeDefinition) ifElseElementsDef).getParentNodeID();
		} else if(ifElseElementsDef instanceof ActionDefinition) {
			parentNodeID = ((ActionDefinition) ifElseElementsDef).getParentNodeID();
		}
		return parentNodeID;
	}

	public IfElseElementsDef getElementByNodeID_Level_One(UUID nodeID) {
		IfElseElementsDef ifElseElemDef = null;
		if(mainIF != null) {
			List<MainIfElementsDef> mainIfElseElemDef = mainIF.getMainIfElementsDef();
			int noOfMainIfElseElements = mainIfElseElemDef.size();
			for(int index = 0; index < noOfMainIfElseElements; index ++) {
				if(mainIfElseElemDef.get(index) instanceof IfDefinition) {
					IfDefinition tempIf = (IfDefinition) mainIfElseElemDef.get(index);
 					if(tempIf.getNodeID().equals(nodeID)) {
						ifElseElemDef = tempIf;
					} else {
						int noOfChilds = tempIf.getIfElseElements().size();
						for(int ifIndex = 0; ifIndex < noOfChilds; ifIndex ++) {
							ifElseElemDef = getElementByNodeID_Level_Two(nodeID, (IfElseElementsDef) mainIfElseElemDef.get(index));
							if(ifElseElemDef != null) {
								break;
							}
						}
					}
				} else if(mainIfElseElemDef.get(index) instanceof ElseIfDefinition) {
					ElseIfDefinition tempElseIf = (ElseIfDefinition) mainIfElseElemDef.get(index);
					if(tempElseIf.getNodeID().equals(nodeID)) {
						ifElseElemDef = tempElseIf;
					} else {
						int noOfChilds = tempElseIf.getIfElseElements().size();
						for(int ifIndex = 0; ifIndex < noOfChilds; ifIndex ++) {
							ifElseElemDef = getElementByNodeID_Level_Two(nodeID, (IfElseElementsDef) mainIfElseElemDef.get(index));
							if(ifElseElemDef != null) {
								break;
							}
						}
					}
				} else if(mainIfElseElemDef.get(index) instanceof ElseDefinition) {
					ElseDefinition tempElse = (ElseDefinition) mainIfElseElemDef.get(index);
					if(tempElse.getNodeID().equals(nodeID)) {
						ifElseElemDef = tempElse;
					} else {
						int noOfChilds = tempElse.getIfElseElements().size();
						for(int ifIndex = 0; ifIndex < noOfChilds; ifIndex ++) {
							ifElseElemDef = getElementByNodeID_Level_Two(nodeID, (IfElseElementsDef) mainIfElseElemDef.get(index));
							if(ifElseElemDef != null) {
								break;
							}
						}
					}
				}
				if(ifElseElemDef != null) {
					break;
				}
			}
		}
		return ifElseElemDef;
	}

	public IfElseElementsDef getElementByNodeID_Level_Two(UUID nodeID, IfElseElementsDef sourceIfElseElement) {
		IfElseElementsDef ifElseElementsDef = null;
		if(sourceIfElseElement != null) {
			if(sourceIfElseElement instanceof IfDefinition) {
				if(((IfDefinition) sourceIfElseElement).getNodeID().equals(nodeID)) {
					ifElseElementsDef = sourceIfElseElement;
				} else {
					ifElseElementsDef = getElementByNodeID_Level_Three_If(nodeID, (IfDefinition) sourceIfElseElement);
				}
			} else if(sourceIfElseElement instanceof ElseIfDefinition) {
				if(((ElseIfDefinition) sourceIfElseElement).getNodeID().equals(nodeID)) {
					ifElseElementsDef = sourceIfElseElement;
				} else {
					ifElseElementsDef = getElementByNodeID_Level_Three_ElseIf(nodeID, (ElseIfDefinition) sourceIfElseElement);
				}
			} else if (sourceIfElseElement instanceof ElseDefinition) {
				if(((ElseDefinition) sourceIfElseElement).getNodeID().equals(nodeID)) {
					ifElseElementsDef = sourceIfElseElement;
				} else {
					ifElseElementsDef = getElementByNodeID_Level_Three_Else(nodeID, (ElseDefinition) sourceIfElseElement);
				}
			}
		}
		return ifElseElementsDef;
	}

	public IfElseElementsDef getElementByNodeID_Level_Three_If(UUID nodeID, IfDefinition ifDef) {
		IfElseElementsDef ifElseElementsDef = null;
		IfElseElementsDef tempDef = null;
		List<IfElseElementsDef> childElements = ifDef.getIfElseElements();
		int noOfElements = childElements.size();
		for(int index = 0; index < noOfElements; index ++) {
			tempDef = childElements.get(index);
			if(! (tempDef instanceof CodeBlockDefinition)) {
				ifElseElementsDef = getElementByNodeID_Level_Two(nodeID, tempDef);
				if(ifElseElementsDef != null) {
					break;
				}
			}
		}
		return ifElseElementsDef;
	}

	public IfElseElementsDef getElementByNodeID_Level_Three_ElseIf(UUID nodeID, ElseIfDefinition elseIfDef) {
		IfElseElementsDef ifElseElementsDef = null;
		IfElseElementsDef tempDef = null;
		List<IfElseElementsDef> childElements = elseIfDef.getIfElseElements();
		int noOfElements = childElements.size();
		for(int index = 0; index < noOfElements; index ++) {
			tempDef = childElements.get(index);
			if(! (tempDef instanceof CodeBlockDefinition)) {
				ifElseElementsDef = getElementByNodeID_Level_Two(nodeID, tempDef);
				if(ifElseElementsDef != null) {
					break;
				}
			}
		}
		return ifElseElementsDef;
	}

	public IfElseElementsDef getElementByNodeID_Level_Three_Else(UUID nodeID, ElseDefinition elseDef) {
		IfElseElementsDef ifElseElementsDef = null;
		IfElseElementsDef tempDef = null;
		List<IfElseElementsDef> childElements = elseDef.getIfElseElements();
		int noOfElements = childElements.size();
		for(int index = 0; index < noOfElements; index ++) {
			tempDef = childElements.get(index);
			if(! (tempDef instanceof CodeBlockDefinition)) {
				ifElseElementsDef = getElementByNodeID_Level_Two(nodeID, tempDef);
				if(ifElseElementsDef != null) {
					break;
				}
			}
		}
		return ifElseElementsDef;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer("\n\n[WebPage name : ");
		sbf.append(name).append(", Type : ").append(type).append(", navigationOrder : ").append(navigationOrder);
		sbf.append(", executeOnce : ").append(executeOnce).append(", browser : ").append(browser);
		sbf.append(", url : ").append(url);
		sbf.append(", Conditions : [");
		if(conditions != null) {
			List<ConditionDefinition> cons = conditions.getConditions();
			for(int conIndex = 0; conIndex < cons.size(); conIndex ++) {
				sbf.append(cons.get(conIndex));
			}
		} else {
			sbf.append(conditions);
		}
		sbf.append("]");
		sbf.append("\nMainIf : [");
		if(mainIF != null) {
			sbf.append(mainIF.toString());
		} else {
			sbf.append(mainIF);
		}
		sbf.append("]]");
		return sbf.toString();
	}

}
