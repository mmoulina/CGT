package com.poplicus.crawler.codegen.definitions;

import java.util.UUID;

//public class LineOfCodeDefinition implements CodeBlockElementsDef {
public class LineOfCodeDefinition implements IfElseElementsDef, MainIfElementsDef {
	
	private UUID nodeID = null;
	
	private UUID parentNodeID = null;
	
	private String name = null;

	private LHSDefinition lhsDefiniton = null;
	
	private RightHSDefinition rhsDefinition = null;

	public LHSDefinition getLhsDefiniton() {
		return lhsDefiniton;
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

	public void setLhsDefiniton(LHSDefinition lhsDefiniton) {
		this.lhsDefiniton = lhsDefiniton;
	}

	public RightHSDefinition getRhsDefinition() {
		return rhsDefinition;
	}

	public void setRhsDefinition(RightHSDefinition rhsDefinition) {
		this.rhsDefinition = rhsDefinition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer("Line Of Code : [");
		sbf.append(lhsDefiniton.toString()).append(", ").append(rhsDefinition.toString());
		sbf.append("]");
		return sbf.toString();
	}
}
