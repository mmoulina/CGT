package com.poplicus.crawler.codegen.definitions;

import java.util.UUID;

import com.poplicus.crawler.codegen.definitions.HTMLElementsDef;

//public class ActionDefinition implements CodeBlockElementsDef {
public class ActionDefinition implements IfElseElementsDef, MainIfElementsDef {
	
	private UUID nodeID = null;
	
	private UUID parentNodeID = null;
	
	private String type = null;
	
	private String browserType = null;
	
	private HTMLElementsDef htmlElement = null;
	
	public HTMLElementsDef getHtmlElement() {
		return htmlElement;
	}

	public void setHtmlElement(HTMLElementsDef htmlElement) {
		this.htmlElement = htmlElement;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public UUID getNodeID() {
		return nodeID;
	}

	public void setNodeID(UUID nodeID) {
		this.nodeID = nodeID;
	}

	public UUID getParentNodeID() {
		return parentNodeID;
	}

	public void setParentNodeID(UUID parentNodeID) {
		this.parentNodeID = parentNodeID;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer("Action Definition - type : ");
		sbf.append(type).append(", ");
		if(htmlElement != null) {
			sbf.append(htmlElement.toString());
		}
		return sbf.toString();
	}

}
