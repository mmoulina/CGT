package com.poplicus.crawler.codegen.gui.components;

import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;

public class PopTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -2919535309856567752L;

	public enum PopTreeNodeType {
		None {
			public String toString() {
				return "None";
			}
		},

		If {
			public String toString() {
				return "If";
			}
		},

		Else {
			public String toString() {
				return "Else";
			}
		},

		Else_If {
			public String toString() {
				return "ElseIf";
			}
		},

		LineOfCode {
			public String toString() {
				return "LineOfCode";
			}
		},

		Action {
			public String toString() {
				return "Action";
			}
		}
	}

	private UUID nodeID = null;

	private UUID parentNodeID = null;

	private PopTreeNodeType nodeType = null;

	private PopTreeNodeType parentNodeType = null;

	private PopTreeNode peerIfNode = null;

	private boolean addedElse = false;

	private boolean closingTag = false;

	private boolean siblingIf = false;

	public PopTreeNode(Object userObject) {
		super(userObject);
		nodeID = UUID.randomUUID();
	}

	public PopTreeNode(Object userObject, PopTreeNodeType nodeType) {
		super(userObject);
		this.nodeType = nodeType;
		nodeID = UUID.randomUUID();
	}

	public PopTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		nodeID = UUID.randomUUID();
	}

	public PopTreeNode(Object userObject, boolean allowsChildren, PopTreeNodeType nodeType) {
		super(userObject, allowsChildren);
		this.nodeType = nodeType;
		nodeID = UUID.randomUUID();
	}

	public UUID getNodeID() {
		return nodeID;
	}

	public void setNodeID(UUID nodeID) {
		this.nodeID = nodeID;
	}

	public PopTreeNodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(PopTreeNodeType nodeType) {
		this.nodeType = nodeType;
	}

	public boolean isClosingTag() {
		return closingTag;
	}

	public void setClosingTag(boolean closingTag) {
		this.closingTag = closingTag;
	}

	public UUID getParentNodeID() {
		return parentNodeID;
	}

	public void setParentNodeID(UUID parentNodeID) {
		this.parentNodeID = parentNodeID;
	}

	public PopTreeNodeType getParentNodeType() {
		return parentNodeType;
	}

	public void setParentNodeType(PopTreeNodeType parentNodeType) {
		this.parentNodeType = parentNodeType;
	}

	public PopTreeNode getPeerIfNode() {
		return peerIfNode;
	}

	public void setPeerIfNode(PopTreeNode peerIfNode) {
		this.peerIfNode = peerIfNode;
	}

	public boolean isAddedElse() {
		return addedElse;
	}

	public void setAddedElse(boolean addedElse) {
		this.addedElse = addedElse;
	}

	public boolean isSiblingIf() {
		return siblingIf;
	}

	public void setSiblingIf(boolean siblingIf) {
		this.siblingIf = siblingIf;
	}

}
