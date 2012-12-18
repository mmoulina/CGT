package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IfDefinition implements IfElseElementsDef, MainIfElementsDef {
	
	private UUID nodeID = null;
	
	private UUID parentNodeID = null;
	
	private Conditions conditions = null;
	
	private List<IfElseElementsDef> ifElseElements = new ArrayList<IfElseElementsDef>();
	
	public void setIfElseElement(IfElseElementsDef ifElseElement) {
		this.ifElseElements.add(ifElseElement);
	}
	
	public List<IfElseElementsDef> getIfElseElements() {
		return this.ifElseElements;
	}
	
	public Conditions getConditions() {
		return conditions;
	}

	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
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

}
