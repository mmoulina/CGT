package com.poplicus.crawler.codegen.gui.components;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.poplicus.crawler.codegen.gui.components.PopTreeNode.PopTreeNodeType;

public class PopTree extends JTree {

	private static final long serialVersionUID = -1649716779288418074L;

	private static FontManager fontManager = FontManager.getInstance();

	private PopTreePopup rootPopup = new PopTreePopup();

	private PopTreePopup ifPopup = new PopTreePopup(true);

	private JFrame containingFrame = null;

	private String uniqueNameOfWebPage = null;

	private String browserTypeOfWebPage = null;

	private String webPageName = null;

	private String sourceID = null;

	public PopTree() {
		super();
		initialize();
		this.setModel(new DefaultTreeModel(new PopTreeNode("Custom Code", PopTreeNodeType.None)));
	}

	public PopTree(String myCode) {
		super();
		initialize();
		this.setModel(new DefaultTreeModel(new PopTreeNode(myCode, PopTreeNodeType.None)));
	}

	public PopTree(TreeNode treeNode) {
		super(treeNode);
		initialize();
	}

	private void initialize() {
		this.setFont(fontManager.getTreeFontPlain());

		this.addMouseListener(new PopTreeMouseAdapter());

		this.putClientProperty("JTree.lineStyle", "None");

		this.setShowsRootHandles(false);
		this.setRowHeight(20);

		((DefaultTreeCellRenderer) this.getCellRenderer()).setOpenIcon(null);
		((DefaultTreeCellRenderer) this.getCellRenderer()).setClosedIcon(null);
		((DefaultTreeCellRenderer) this.getCellRenderer()).setLeafIcon(null);

		UIManager.put("Tree.collapsedIcon", new IconUIResource(new PopNodeIcon("collapse")));
		UIManager.put("Tree.expandedIcon",  new IconUIResource(new PopNodeIcon("expand")));
	}

	public PopTree getPopTree() {
		return this;
	}

	public JFrame getContainingFrame() {
		return containingFrame;
	}

	public void setContainingFrame(JFrame containingFrame) {
		this.containingFrame = containingFrame;
	}

	public String getUniqueNameOfWebPage() {
		return uniqueNameOfWebPage;
	}

	public void setUniqueNameOfWebPage(String uniqueNameOfWebPage) {
		this.uniqueNameOfWebPage = uniqueNameOfWebPage;
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

	public String getBrowserTypeOfWebPage() {
		return browserTypeOfWebPage;
	}

	public void setBrowserTypeOfWebPage(String browserTypeOfWebPage) {
		this.browserTypeOfWebPage = browserTypeOfWebPage;
	}

	@Override
	public void updateUI() {
		super.updateUI();
		initialize();
	}

	public void showPopup(PopTreeNode parentNode, PopTreePopup popup, Component source, int x, int y) {
		popup.show(source, x, y);
		popup.setRootTree(getPopTree());
		popup.setSourceID(sourceID);
		popup.setUniqueNameOfWebPage(uniqueNameOfWebPage);
		popup.setBrowserTypeOfWebPage(browserTypeOfWebPage);
		popup.setWebPageName(webPageName);
		popup.setParentNode(parentNode);
	}

	class PopTreeMouseAdapter extends MouseAdapter {

		@Override
		public void mouseReleased(MouseEvent e) {
			PopTree jTree = (PopTree) e.getSource();
	        TreePath selPath = jTree.getPathForLocation(e.getX(), e.getY());
	        if(selPath == null) {
	            return;
	        } else {
	            jTree.setSelectionPath(selPath);
		        if(e.isPopupTrigger()) {
		        	PopTreeNode nodeOnPopupTriggered = (PopTreeNode) selPath.getLastPathComponent();
		        	if(! nodeOnPopupTriggered.isClosingTag()) {
		        		if(nodeOnPopupTriggered.isRoot()) {
			        		showPopup(null, rootPopup, (Component) e.getSource(), e.getX(), e.getY());
			        	} else if(nodeOnPopupTriggered.getNodeType().equals(PopTreeNodeType.If)
			        		|| nodeOnPopupTriggered.getNodeType().equals(PopTreeNodeType.Else_If)
			        		|| nodeOnPopupTriggered.getNodeType().equals(PopTreeNodeType.Else)){
			        		ifPopup.setPeerIFNode(null);

		        			ifPopup.setDisableIf_If(true);
		        			ifPopup.setDisableElseIf_If(true);
		        			ifPopup.setDisableElse_If(true);

		        			if(nodeOnPopupTriggered.getNodeType().equals(PopTreeNodeType.If)) {
		        				ifPopup.setDisableIf_If(false);
		        				ifPopup.setPeerIFNode(nodeOnPopupTriggered);
		        				if(nodeOnPopupTriggered.isAddedElse()) {
		        					ifPopup.setDisableElseIf_If(false);
		        					ifPopup.setDisableElse_If(false);
		        				}
		        				if(nodeOnPopupTriggered.isSiblingIf()) {
		        					ifPopup.setDisableElseIf_If(false);
		        					ifPopup.setDisableElse_If(false);
		        				}
		        			} else if(nodeOnPopupTriggered.getNodeType().equals(PopTreeNodeType.Else_If)) {
			        			ifPopup.setDisableIf_If(false);
			        			ifPopup.setPeerIFNode(nodeOnPopupTriggered.getPeerIfNode());
			        			if(nodeOnPopupTriggered.getPeerIfNode() != null) {
			        				if(nodeOnPopupTriggered.getPeerIfNode().isAddedElse()) {
			        					ifPopup.setDisableElseIf_If(false);
			        					ifPopup.setDisableElse_If(false);
			        				}
			        				if(nodeOnPopupTriggered.isSiblingIf()) {
			        					ifPopup.setDisableElseIf_If(false);
			        					ifPopup.setDisableElse_If(false);
			        				}
			        			}
			        		} else if(nodeOnPopupTriggered.getNodeType().equals(PopTreeNodeType.Else)) {
			        			ifPopup.setDisableIf_If(false);
			        			ifPopup.setDisableElseIf_If(false);
			        			ifPopup.setDisableElse_If(false);
			        			ifPopup.setPeerIFNode(nodeOnPopupTriggered.getPeerIfNode());

			        			if(nodeOnPopupTriggered.getPeerIfNode() != null) {
			        				nodeOnPopupTriggered.getPeerIfNode().setAddedElse(true);
			        			}
		        				if(nodeOnPopupTriggered.isSiblingIf()) {
		        					ifPopup.setDisableElseIf_If(false);
		        					ifPopup.setDisableElse_If(false);
		        				}
			        		}
		        			showPopup(nodeOnPopupTriggered, ifPopup, (Component) e.getSource(), e.getX(), e.getY());
			        	}
		        	}
		        }
	        }
		}

	}

}
