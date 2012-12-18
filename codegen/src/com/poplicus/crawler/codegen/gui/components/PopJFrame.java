package com.poplicus.crawler.codegen.gui.components;

import javax.swing.JFrame;

import com.poplicus.crawler.codegen.gui.components.PopTreeNode.PopTreeNodeType;

public class PopJFrame extends JFrame {

	private static final long serialVersionUID = 57330919469183643L;

	private PopTreeNodeType nodeType = null;
	
	private String uniqueNameOfWebPage = null;
	
	private String browserTypeOfWebPage = null;
	
	private String webPageName = null;
	
	private String sourceID = null;
	
	private PopTreeNode parentNode = null;
	
	private PopTreeNode peerIfNode = null;

	public PopTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(PopTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public PopTreeNodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(PopTreeNodeType nodeType) {
		this.nodeType = nodeType;
	}
	
	public String getUniqueNameOfWebPage() {
		return uniqueNameOfWebPage;
	}

	public void setUniqueNameOfWebPage(String uniqueNameOfWebPage) {
		this.uniqueNameOfWebPage = uniqueNameOfWebPage;
	}
	
	public String getBrowserTypeOfWebPage() {
		return browserTypeOfWebPage;
	}

	public void setBrowserTypeOfWebPage(String browserTypeOfWebPage) {
		this.browserTypeOfWebPage = browserTypeOfWebPage;
	}

	public String getSourceID() {
		return sourceID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}
	
	public String getWebPageName() {
		return webPageName;
	}

	public void setWebPageName(String webPageName) {
		this.webPageName = webPageName;
	}

	public PopTreeNode getPeerIfNode() {
		return peerIfNode;
	}

	public void setPeerIfNode(PopTreeNode peerIfNode) {
		this.peerIfNode = peerIfNode;
	}

}
